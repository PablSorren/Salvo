package com.codeoftheweb.salvo;


import com.codeoftheweb.salvo.entities.Game;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.entities.Player;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

//-----------------------------------------------List of Singed up Players--------------------------------------------------------
   @GetMapping("/players")
    public List<Map<String, Object>> getPlayers() {

        return playerRepository
                .findAll()
                .stream()
                .map(Player::toDTO)
                .collect(Collectors.toList());
    }

//-----------------------------------------------Sing up a new Player----------------------------------------------------------------
    @PostMapping("/players")
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {

        if(email.isEmpty()) {
            return new ResponseEntity<>(toMap("error","Email not specified"), HttpStatus.FORBIDDEN);
        }
        if(password.isEmpty()){
            return new ResponseEntity<>(toMap("error","Must specify a password"), HttpStatus.FORBIDDEN);
        }

        if(playerRepository.findByEmail(email) != null){
            return new ResponseEntity<>(toMap("error","Email already taken"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(toMap("email", email), HttpStatus.CREATED);
    }

//------------------------------------Game view of a game by a URL-path-variable-gameplayer's id----------------------------------------
    @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> gamePlayer(@PathVariable long gamePlayerId, Authentication authentication) {

        Player currentPlayer = playerRepository.findByEmail(authentication.getName());
        Optional<GamePlayer> gp = gamePlayerRepository.findById(gamePlayerId);
        ResponseEntity<Map<String, Object>> response;

        if (gp.isEmpty()) {

            response = new ResponseEntity<>(toMap("ERROR", String.format("Game player id %d does not exists", gamePlayerId))
                    , HttpStatus.UNAUTHORIZED);

        } else if(isNotLogged(authentication)) {
            response = guestUnauthorizedWarning();

        } else if( currentPlayer.getUserId() != gp.get().getPlayerId()) {

            response = new ResponseEntity<>(toMap("ERROR", "PERMISSION DENIED - NOT YOUR GAME")
                                           , HttpStatus.UNAUTHORIZED);
       } else {

            response = new ResponseEntity<>(gp.get().gameView(), HttpStatus.OK);
        }

        return response;
    }

    //-----------------------------------------------List of Games--------------------------------------------------------

    @GetMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {

        Map<String, Object> dto = new LinkedHashMap<>();

        if(isNotLogged(authentication)) {
            dto.put("player", "Guest");
        } else {
            dto.put("player", playerRepository.findByEmail(authentication.getName()).toDTO());
        }

        dto.put("games",  gameRepository
                            .findAll()
                            .stream()
                            .map(Game::toDTO)
                            .collect(Collectors.toList()));
        return dto;
    }
//-----------------------------------------------GAME CREATION-----------------------------------------------------------------

    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {

       ResponseEntity<Map<String, Object>> response;

       if(isNotLogged(authentication)) {
           response = guestUnauthorizedWarning() ;

       } else {
          Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
          Player currentPlayer = playerRepository.findByEmail(authentication.getName());
          GamePlayer gp = gamePlayerRepository.save(new GamePlayer(currentPlayer, newGame));
          response = new ResponseEntity<>(toMap("gpid", gp.getId()), HttpStatus.CREATED);

       }
    return response;
    }


//-----------------------------------------------JOIN A GAME----------------------------------------------------------------

    @RequestMapping("/game/{gameId}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long gameId, Authentication authentication){

        ResponseEntity<Map<String, Object>> response;

        Optional<Game> game = gameRepository.findById(gameId);

    if(game.isEmpty()) {
        response = new ResponseEntity<>(toMap("Error", "Game not found"), HttpStatus.FORBIDDEN);

    } else if(isNotLogged(authentication)) {
           response = guestUnauthorizedWarning();

    }  else {

        Player currentPlayer = playerRepository.findByEmail(authentication.getName());
        GamePlayer gp = gamePlayerRepository.save(new GamePlayer(currentPlayer, game.get()));

        if (game.get().isFull() && currentPlayer.getUserId() != gp.getPlayerId()) {
            response = new ResponseEntity<>(toMap("ERROR", "GAME is full"), HttpStatus.FORBIDDEN);

        } else {
            response = new ResponseEntity<>(toMap("gpid", gp.getId()), HttpStatus.CREATED);
        }
    }

    return response;
    }

//-----------------------------------------------PRIVATE CONTROLLER'S FUNCTIONS----------------------------------------------------------

    private boolean isNotLogged(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    private <K, V> Map<K, V> toMap(K key, V value) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    private ResponseEntity<Map<String, Object>> guestUnauthorizedWarning() {
       return new ResponseEntity<>(toMap("WARNING", "Guests are not authorized here"), HttpStatus.UNAUTHORIZED);
    }


}
