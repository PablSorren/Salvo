package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.Util;
import com.codeoftheweb.salvo.entities.*;
import com.codeoftheweb.salvo.repositories.*;
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
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    SalvoRepository salvoRepository;

    @Autowired
    ScoreRepository scoreRepository;

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
            dto.put("gameState", getGameStates(gp.get()).toString());

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




            GamePlayer opponent = gp.get().getOpponent();

            if(opponent == null) { //evita el error que te tira al entrar a un game sin un oponente
                hits.put("self", new ArrayList<>());
                hits.put("opponent",  new ArrayList<>());

            } else {
                hits.put("self", gp.get().listOfHitsAndMisses());
                hits.put("opponent",  opponent.listOfHitsAndMisses());
            }


            dto.put("hits", hits);


            response = new ResponseEntity<>(dto, HttpStatus.OK);

        }


        return response;
    }


//------------------------------------CHECK THE GAME STATE AND ASSING THE RESULT----------------------------------------


    public GameStates getGameStates(GamePlayer self) {

        GameStates gameState ;

        if(self.getOpponent() == null) {
            gameState = GameStates.WAITINGFOROPP;

        } else if(!self.shipsPlaced()) {
            gameState = GameStates.PLACESHIPS;

        } else if (!self.getOpponent().shipsPlaced()) {
            gameState = GameStates.WAIT;

        } else {

            if(self.checkIfAllShipsSunk()) {

                if (self.getOpponent().checkIfAllShipsSunk() && (self.getSalvoes().size() >= self.getOpponent().getSalvoes().size())) {
                    gameState = GameStates.TIE;
                    saveNewScore(self,gameState);

                } else {
                    gameState = GameStates.LOST;
                    saveNewScore(self,gameState);

                }

            } else if(self.getOpponent().checkIfAllShipsSunk() && (self.getSalvoes().size() <= self.getOpponent().getSalvoes().size())){
                gameState = GameStates.WON;
                saveNewScore(self,gameState);

            }  else if (Util.opponentTurn(self)) {
                gameState = GameStates.WAIT;

            } else {
                gameState = GameStates.PLAY;

            }

        }

        return gameState;
    }

    private void saveNewScore(GamePlayer gamePlayer, GameStates gameState) {

        Score score = null;

        Player player = gamePlayer.getPlayer();
        Game game = gamePlayer.getGame();

        switch (gameState) {

            case TIE: score = new Score(player, game, 0.5);
            break;

            case WON: score = new Score(player, game, 1);
                break;

            case LOST: score = new Score(player, game, 0);
                break;

        }

        game.addScore(score);
        gameRepository.save(game);


        player.addScore(score);
        playerRepository.save(player);

        scoreRepository.save(score);
        gamePlayerRepository.save(gamePlayer);

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

