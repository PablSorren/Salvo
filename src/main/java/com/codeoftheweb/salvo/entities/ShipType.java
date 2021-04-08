package com.codeoftheweb.salvo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShipType {

    @JsonProperty("carrier")
    CARRIER(5),
    @JsonProperty("battleship")
    BATTLESHIP(4),
    @JsonProperty("submarine")
    SUBMARINE(3),
    @JsonProperty("destroyer")
    DESTROYER(3),
    @JsonProperty("patrolboat")
    PATROL_BOAT(2) ;

    int celdas;

    ShipType(int length) {
        this.celdas = length;
    }

    int getLength(){
        return celdas;
    }
}

