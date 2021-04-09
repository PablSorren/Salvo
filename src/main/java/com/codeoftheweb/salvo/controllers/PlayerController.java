package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.Util;
import com.codeoftheweb.salvo.entities.Player;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api")
public class PlayerController {

//-----------------------------------------------Autowired Classes--------------------------------------------------------

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

//-----------------------------------------------List of Singed up Players--------------------------------------------------------
    @GetMapping("/players")
    public List<Map<String, Object>> getPlayers() {

        return playerRepository
                .findAll()
                .stream()
                .map(Player::toDTO)
                .collect(Collectors.toList());
    }


//-----------------------------------------------Sing up a new Player----------------------------------------------------------------
    @PostMapping("/players")
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {

        if(email.isEmpty()) {
            return new ResponseEntity<>(Util.toMap("error","Email not specified"), HttpStatus.FORBIDDEN);
        }
        if(password.isEmpty()){
            return new ResponseEntity<>(Util.toMap("error","Must specify a password"), HttpStatus.FORBIDDEN);
        }

        if(playerRepository.findByUsername(email) != null){
            return new ResponseEntity<>(Util.toMap("error","Email already taken"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(Util.toMap("email", email), HttpStatus.CREATED);
    }



}
