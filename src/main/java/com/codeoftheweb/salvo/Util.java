package com.codeoftheweb.salvo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.LinkedHashMap;
import java.util.Map;

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

}
