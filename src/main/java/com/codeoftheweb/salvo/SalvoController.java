package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

     @Autowired
    GameRepository gameRepository;


    @RequestMapping("/games")
     public List< Map<String, Object> > getGames(){
         return gameRepository
                 .findAll()
                 .stream()
                 .map(game -> gamesDTO(game))
                 .collect(Collectors.toList());
     }


     private Map<String, Object> gamesDTO(Game game){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getGameId() );
        dto.put("created", game.getDateAndTimeOfCreation());
        dto.put("gamePlayers", getGamePlayers(game));
        return  dto;
     }

    private List< Map<String, Object> > getGamePlayers(Game game){
        return   game
                .getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayerDTO(gamePlayer))
                .collect(Collectors.toList());
     }


     private Map<String, Object> gamePlayerDTO(GamePlayer gamePlayer){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player" , playerDTO(gamePlayer.getPlayer()));
        return dto;
     }

    private Map<String, Object> playerDTO(Player player){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("email" , player.getUserName());
        return dto;
    }


}
