package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entities.Game;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.entities.Player;
import com.codeoftheweb.salvo.entities.Salvo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Util {


    public static boolean isNotLogged(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    public static <K, V> Map<K, V> toMap(K key, V value) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    public static ResponseEntity<Map<String, Object>> guestUnauthorizedWarning() {
        return new ResponseEntity<>(toMap("WARNING", "Guests are not authorized here"), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<Map<String, Object>> deniedGameView() {
        return new ResponseEntity<>(Util.toMap("ERROR", "PERMISSION DENIED - NOT YOUR GAME")
                , HttpStatus.UNAUTHORIZED);
    }


    public static boolean opponentTurn(GamePlayer currentGamePlayer){
        boolean isOpponentTurn = true;

        GamePlayer opponent = currentGamePlayer.getOpponent();

        if(currentGamePlayer.getSalvoes().size() <= opponent.getSalvoes().size()) {
            isOpponentTurn = false;
        }
        return isOpponentTurn;
    }

    public static boolean repeatedTurn(GamePlayer gp, int salvoTurn) {
        boolean isRepeatedTurn = false;

        for (Salvo salvo : gp.getSalvoes()){

            if(salvo.getTurn() == salvoTurn) {
               isRepeatedTurn  = true;
               break;
            }
        }

        return  isRepeatedTurn;
    }



}
