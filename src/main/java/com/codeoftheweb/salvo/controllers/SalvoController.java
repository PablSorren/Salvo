package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.Util;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.entities.Player;
import com.codeoftheweb.salvo.entities.Salvo;
import com.codeoftheweb.salvo.entities.Ship;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.codeoftheweb.salvo.repositories.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

//-----------------------------------------------Autowired Classes--------------------------------------------------------

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

            response = Util.deniedGameView();
       } else {

            /*********************methods to see the ships in the frontend***************************/

            Map<String, Object> dto = new LinkedHashMap<>();
            Map<String, Object> hits = new LinkedHashMap<>();
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());

            dto.put("id", gp.get().getGame().getGameId());
            dto.put("created", gp.get().getGame().getDateAndTimeOfCreation());
            dto.put("gameState", "PLACESHIPS");

            dto.put("gamePlayers", gp.get().getGame().getGamePlayers()
                    .stream()
                    .map(GamePlayer::toDTO)
                    .collect(Collectors.toList()));

            dto.put("ships", gp.get().getShips()
                    .stream()
                    .map(Ship::toDTO)
                    .collect(Collectors.toList()));

            dto.put("salvoes", gp.get().getGame().getGamePlayers()
                    .stream()
                    .flatMap(gamePlayer1 -> gamePlayer1.getSalvoes()
                            .stream()
                            .map(Salvo::toDto))
                    .collect(Collectors.toList()));

            dto.put("hits", hits);


            response = new ResponseEntity<>(dto, HttpStatus.OK);

        }


        return response;
    }


}

