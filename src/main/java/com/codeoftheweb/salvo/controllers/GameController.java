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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GameController {

    //-----------------------------------------------Autowired Classes--------------------------------------------------------

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    //-----------------------------------------------GAME CREATION-----------------------------------------------------------------

    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {

        ResponseEntity<Map<String, Object>> response;

        if(Util.isNotLogged(authentication)) {
            response = Util.guestUnauthorizedWarning() ;

        } else {
            Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
            Player currentPlayer = playerRepository.findByEmail(authentication.getName());
            GamePlayer gp = gamePlayerRepository.save(new GamePlayer(currentPlayer, newGame));
            response = new ResponseEntity<>(Util.toMap("gpid", gp.getId()), HttpStatus.CREATED);

        }
        return response;
    }


//-----------------------------------------------JOIN A GAME----------------------------------------------------------------

    @RequestMapping("/game/{gameId}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long gameId, Authentication authentication){

        ResponseEntity<Map<String, Object>> response;

        Optional<Game> game = gameRepository.findById(gameId);

        if(game.isEmpty()) {
            response = new ResponseEntity<>(Util.toMap("Error", "Game not found"), HttpStatus.FORBIDDEN);

        } else if(Util.isNotLogged(authentication)) {
            response = Util.guestUnauthorizedWarning();

        }  else {

            Player currentPlayer = playerRepository.findByEmail(authentication.getName());
            GamePlayer gp = gamePlayerRepository.save(new GamePlayer(currentPlayer, game.get()));

            if (game.get().isFull() && currentPlayer.getUserId() != gp.getPlayerId()) {
                response = new ResponseEntity<>(Util.toMap("ERROR", "GAME is full"), HttpStatus.FORBIDDEN);

            } else {
                response = new ResponseEntity<>(Util.toMap("gpid", gp.getId()), HttpStatus.CREATED);
            }
        }

        return response;
    }




}
