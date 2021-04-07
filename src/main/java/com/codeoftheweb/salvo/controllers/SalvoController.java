package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.Util;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.entities.Player;
import com.codeoftheweb.salvo.entities.Ship;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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

            response = new ResponseEntity<>(gp.get().gameView(), HttpStatus.OK);
        }

        return response;
    }

//------------------------------------Ships Placement----------------------------------------------------

    @RequestMapping(value="/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addShips(@PathVariable long gamePlayerId, @RequestBody Set<Ship> ships, Authentication authentication) {

        ResponseEntity<Map<String,Object>> response;
        Optional<GamePlayer> gp = gamePlayerRepository.findById(gamePlayerId);
        Player currentPlayer = playerRepository.findByEmail(authentication.getName());

        if(gp.isEmpty()) {
            response = new ResponseEntity<>(Util.toMap("ERROR", String.format("Game player id %d does not exists", gamePlayerId))
                                            , HttpStatus.UNAUTHORIZED);

        } else if(Util.isNotLogged(authentication)) {
            response = Util.guestUnauthorizedWarning();

        } else if( currentPlayer.getUserId() != gp.get().getPlayerId()) {
            response = Util.deniedGameView();

        } else if(gp.get().shipsPlaced()){
            response = new ResponseEntity<>(Util.toMap("DENIED", "SHIPS ALREADY PLACED")
                    , HttpStatus.FORBIDDEN);
        } else {

            gp.get().addShips(ships);
            gamePlayerRepository.save(gp.get());
            response = new ResponseEntity<>(Util.toMap("OK", "SHIPS PLACED"),HttpStatus.CREATED);
        }

        return response;
    }


}
