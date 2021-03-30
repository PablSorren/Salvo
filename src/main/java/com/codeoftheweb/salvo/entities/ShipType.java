package com.codeoftheweb.salvo.entities;

public enum ShipType {

    Carrier(5), Battleship(4), Submarine(3), Destroyer(3),Patrol_Boat(2) ;

    int length;

    ShipType(int length) {
        this.length = length;
    }

    int getLength(){
        return length;
    }
}
