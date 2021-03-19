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
     public List< Map<String, Object> > getGamesId(){
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
        return  dto;
     }



}
