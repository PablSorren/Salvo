package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.Util;
import com.codeoftheweb.salvo.entities.Game;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.entities.Player;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

    //-----------------------------------------------Autowired Classes--------------------------------------------------------

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;


//------------------------------------Game view of a game by a URL-path-variable-gameplayer's id----------------------------------------
    @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> gamePlayer(@PathVariable long gamePlayerId, Authentication authentication) {

        Player currentPlayer = playerRepository.findByEmail(authentication.getName());
        Optional<GamePlayer> gp = gamePlayerRepository.findById(gamePlayerId);
        ResponseEntity<Map<String, Object>> response;

        if (gp.isEmpty()) {

            response = new ResponseEntity<>(Util.toMap("ERROR", String.format("Game player id %d does not exists", gamePlayerId))
                    , HttpStatus.UNAUTHORIZED);

        } else if(Util.isNotLogged(authentication)) {
            response = Util.guestUnauthorizedWarning();

        } else if( currentPlayer.getUserId() != gp.get().getPlayerId()) {

            response = new ResponseEntity<>(Util.toMap("ERROR", "PERMISSION DENIED - NOT YOUR GAME")
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

        if(Util.isNotLogged(authentication)) {
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


}
