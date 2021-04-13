package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.Util;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.entities.Player;
import com.codeoftheweb.salvo.entities.Salvo;
import com.codeoftheweb.salvo.entities.Ship;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.codeoftheweb.salvo.repositories.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

//-----------------------------------------------Autowired Classes--------------------------------------------------------

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    SalvoRepository salvoRepository;

//------------------------------------Game view of a game by a URL-path-variable-gameplayer's id----------------------------------------
    @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> gamePlayer(@PathVariable long gamePlayerId, Authentication authentication) {

        Player currentPlayer = playerRepository.findByUsername(authentication.getName());
        Optional<GamePlayer> gp = gamePlayerRepository.findById(gamePlayerId);
        ResponseEntity<Map<String, Object>> response;

        if (gp.isEmpty()) {

            response = new ResponseEntity<>(Util.toMap("error", String.format("Game player id %d does not exists", gamePlayerId))
                    , HttpStatus.UNAUTHORIZED);

        } else if(Util.isNotLogged(authentication)) {
            response = Util.guestUnauthorizedWarning();

        } else if( currentPlayer.getId() != gp.get().getPlayerId()) {

            response = Util.deniedGameView();
       } else {

            /*********************methods to see the ships in the frontend***************************/

            Map<String, Object> dto = new LinkedHashMap<>();
            Map<String, Object> hits = new LinkedHashMap<>();

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
                                 .map(Salvo::toDTO))
                                 .collect(Collectors.toList()));


            hits.put("self", gp.get().listOfHitsAndMisses());

            GamePlayer opponent = gp.get().getOpponent();

            if(opponent == null) { //evita el error que te tira al entrar a un game sin un oponente
                hits.put("opponent",  new ArrayList<>());
            } else {
                hits.put("opponent",  opponent.listOfHitsAndMisses());
            }


            dto.put("hits", hits);


            response = new ResponseEntity<>(dto, HttpStatus.OK);

        }


        return response;
    }




//------------------------------------Salvos Placement----------------------------------------------------

    @RequestMapping(value="/games/players/{gamePlayerId}/salvoes", method= RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addSalvos(@PathVariable long gamePlayerId,
                                                        @RequestBody Salvo salvo,
                                                        Authentication authentication) {

        ResponseEntity<Map<String,Object>> response;
        Optional<GamePlayer> gp = gamePlayerRepository.findById(gamePlayerId);
        Player currentPlayer = playerRepository.findByUsername(authentication.getName());

        if(gp.isEmpty()) {
            response = new ResponseEntity<>(Util.toMap("error", String.format("Game player id %d does not exists", gamePlayerId))
                    , HttpStatus.UNAUTHORIZED);

        } else if(Util.isNotLogged(authentication)) {
            response = Util.guestUnauthorizedWarning();

        } else if( currentPlayer.getId() != gp.get().getPlayerId()) {
            response = Util.deniedGameView();

        } else if( !gp.get().getGame().isFull()){

            response = new ResponseEntity<>(Util.toMap("error", "you don't have an opponent yet")
                    , HttpStatus.FORBIDDEN);

        } else if(Util.opponentTurn(gp.get())){
            response = new ResponseEntity<>(Util.toMap("error", "not your turn")
                    , HttpStatus.FORBIDDEN);

        } else {

            int salvoTurn = gp.get().getSalvoes().size() + 1;

            if (Util.repeatedTurn(gp.get(), salvoTurn)) {
                response = new ResponseEntity<>(Util.toMap("error", "salvo already submited for this turn")
                        , HttpStatus.FORBIDDEN);

            } else if (salvo.getSalvoLocations().size() != 5) {
                response = new ResponseEntity<>(Util.toMap("error", "5 salvoes must to be shoot")
                        , HttpStatus.FORBIDDEN);

            } else {
                salvo.setTurn(salvoTurn);

                salvoRepository.save(salvo);
                salvo.setGamePlayer(gp.get());

                gp.get().addSalvo(salvo);
                gamePlayerRepository.save(gp.get());

                response = new ResponseEntity<>(Util.toMap("OK", "salvo fired!!"), HttpStatus.CREATED);

            }

        }

        return response;
    }


}

