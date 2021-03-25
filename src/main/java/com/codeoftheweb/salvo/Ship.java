package com.codeoftheweb.salvo;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private ShipType type;

    @ElementCollection
    @Column(name = "locations")
    private List<String> locations ;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship(){}

    public Ship(ShipType type, List<String> locations, GamePlayer gamePlayer){
        this.type = type;
        this.locations = locations;
        this.gamePlayer = gamePlayer;
        this.gamePlayer.addShip(this);
    }

    public GamePlayer getGamePlayer(){
        return gamePlayer;
    }

    public List<String> getLocations(){
        return locations;
    }

    public long getShipId(){
        return id;
    }

    public ShipType getShipType(){
        return type;
    }

    public Long getPlayerId(){
        return gamePlayer.getPlayerId();
    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", type);
        dto.put("locations" , locations);
        return dto;
    }

}
