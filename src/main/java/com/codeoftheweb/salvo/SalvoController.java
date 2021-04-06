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


   @GetMapping("/players")
    public List<Map<String, Object>> getPlayers() {

        return playerRepository
                .findAll()
                .stream()
                .map(Player::toDTO)
                .collect(Collectors.toList());
    }

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


    @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> gamePlayer(@PathVariable long gamePlayerId) {

        Optional<GamePlayer> gp = gamePlayerRepository.findById(gamePlayerId);

        ResponseEntity<Map<String, Object>> response;

        if (gp.isEmpty()) {

            response = new ResponseEntity<>(toMap("ERROR", String.format("Game player id %d does not exists", gamePlayerId))
                                            , HttpStatus.UNAUTHORIZED);
        } else {

            response = new ResponseEntity<>(gp.get().gameView(), HttpStatus.OK);
        }

        return response;
    }

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

    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {

       ResponseEntity<Map<String, Object>> response;

       if(isNotLogged(authentication)) {
           response = new ResponseEntity<>(toMap("player", "guest"), HttpStatus.UNAUTHORIZED);

       } else {
          Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
          Player currentPlayer = playerRepository.findByEmail(authentication.getName());
          GamePlayer gp = gamePlayerRepository.save(new GamePlayer(currentPlayer, newGame));
          response = new ResponseEntity<>(toMap("gpid", gp.getId()), HttpStatus.CREATED);

       }
    return response;
    }


    private boolean isNotLogged(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    private <K, V> Map<K, V> toMap(K key, V value) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }



}
