package com.codeoftheweb.salvo.entities;

import org.hibernate.annotations.GenericGenerator;
import java.util.*;
import javax.persistence.*;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_Id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "salvo_locations")
    private List<String> locations;

    private int turn;

    public Salvo(){}

    public Salvo(GamePlayer gamePlayer, int turn,  List<String> locations){
        this.gamePlayer = gamePlayer;
        gamePlayer.addSalvo(this);
        this.locations = locations;
        this.turn = turn;
    }

    public long getId() {
        return id;
    }

    public List<String> getLocations() {
        return locations;
    }

    public int getTurn(){
        return turn;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }


    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", turn);
        dto.put("player", gamePlayer.getPlayerId());
        dto.put("locations" , locations);
        return dto;
    }


}
