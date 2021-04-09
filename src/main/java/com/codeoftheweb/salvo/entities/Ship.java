package com.codeoftheweb.salvo.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ElementCollection
    @Column(name = "ship_locations")
    private List<String> locations ;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private ShipType type;

    public Ship(){}


    public Ship(ShipType type, List<String> locations, GamePlayer gamePlayer){
        this.type = type;
        this.locations = locations;
        this.gamePlayer = gamePlayer;
    }

    public void setGamePlayer(GamePlayer gp){
        this.gamePlayer = gp;
    }

    public long getShipId(){
        return id;
    }

    public GamePlayer getGamePlayer(){
        return gamePlayer;
    }

    public List<String> getLocations(){
        return locations;
    }

    public ShipType getShipType(){
        return type;
    }

    public void setShipType(ShipType type) {
        this.type = type;
    }

    public Long getPlayerId(){
        return gamePlayer.getPlayerId();
    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", type);
        dto.put("id", id);
        dto.put("locations" , locations);

        return dto;
    }

}
