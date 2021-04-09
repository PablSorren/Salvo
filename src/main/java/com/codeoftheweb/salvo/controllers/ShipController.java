package com.codeoftheweb.salvo.controllers;
import com.codeoftheweb.salvo.Util;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.entities.Player;
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
public class ShipController {

    //-----------------------------------------------Autowired Classes--------------------------------------------------------

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    ShipRepository shipRepository;


//------------------------------------Ships Placement----------------------------------------------------

    @RequestMapping(value="/games/players/{gamePlayerId}/ships", method= RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addShips(@PathVariable long gamePlayerId,
                                                       @RequestBody Set<Ship> ships,
                                                       Authentication authentication) {

        ResponseEntity<Map<String,Object>> response;
        Optional<GamePlayer> gp = gamePlayerRepository.findById(gamePlayerId);
        Player currentPlayer = playerRepository.findByUsername(authentication.getName());

        if(gp.isEmpty()) {
            response = new ResponseEntity<>(Util.toMap("error", String.format("Game player id %d does not exists", gamePlayerId))
                    , HttpStatus.UNAUTHORIZED);

        } else if(Util.isNotLogged(authentication)) {
            response = Util.guestUnauthorizedWarning();

        } else if( currentPlayer.getUserId() != gp.get().getPlayerId()) {
            response = Util.deniedGameView();

        } else if(gp.get().shipsPlaced()){
            response = new ResponseEntity<>(Util.toMap("error", "SHIPS ALREADY PLACED")
                    , HttpStatus.FORBIDDEN);
        } else {


            //gp.get().addShips(ships);

            ships.stream().forEach(ship -> {
                ship.setGamePlayer(gp.get());
                shipRepository.save(ship);

            });

            gamePlayerRepository.save(gp.get());
            response = new ResponseEntity<>(Util.toMap("OK","Ship created"),HttpStatus.CREATED);

        }

        return response;
    }


}
