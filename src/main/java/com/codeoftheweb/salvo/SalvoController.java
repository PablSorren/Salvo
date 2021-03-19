package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

     @Autowired
    GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
     public List<Object> getGamesId(){
         return gamePlayerRepository
                 .findAll()
                 .stream()
                 .map(gamePlayer -> gamePlayer.getGame().getGameId())
                 .collect(Collectors.toList());
     }




}
