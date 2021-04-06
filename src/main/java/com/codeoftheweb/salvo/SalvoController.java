package com.codeoftheweb.salvo;


import com.codeoftheweb.salvo.entities.Game;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.entities.Player;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {

        if(email.isEmpty()) {
            return new ResponseEntity<>("Email not specified", HttpStatus.FORBIDDEN);
        }
        if(password.isEmpty()){
            return new ResponseEntity<>("Must specify a password", HttpStatus.FORBIDDEN);
        }

        if(playerRepository.findByEmail(email) != null){
            return new ResponseEntity<>("Email already taken", HttpStatus.FORBIDDEN);
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

        Player loggedInPlayer = getLoggedInPlayer(authentication);

        if(loggedInPlayer != null) {
            dto.put("player", loggedInPlayer.toDTO());
        } else {
            dto.put("player", "Guest");
        }

        dto.put("games",  gameRepository
                            .findAll()
                            .stream()
                            .map(Game::toDTO)
                            .collect(Collectors.toList()));
        return dto;
    }

    private Player getLoggedInPlayer(Authentication authentication) {

        if(authentication == null) {
            return null;
        } else {
            return playerRepository.findByEmail(authentication.getName());
        }
    }


    private <K, V> Map<K, V> toMap(K key, V value) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }



}
