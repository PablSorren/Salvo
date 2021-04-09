package com.codeoftheweb.salvo.entities;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.ObjectError;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> scores;

    private LocalDateTime dateAndTimeOfCreation;



    public Game() {
        gamePlayers = new HashSet<>();
        scores = new HashSet<>();
    }

    public Game(LocalDateTime time) {
        this.dateAndTimeOfCreation = time;
    }

    public long getGameId() {
        return id;
    }

    public LocalDateTime getDateAndTimeOfCreation(){
        return dateAndTimeOfCreation;
    }


    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public List<Player> getPlayers(){
        return gamePlayers.stream().map(p->p.getPlayer()).collect(Collectors.toList());
    }

    public Set<Ship> getShips(){
        Set<Ship> ships = new HashSet<>();

        for(GamePlayer gp : gamePlayers) {

           Set<Ship> tempShipSet = gp.getShips();

           for(Ship ship : tempShipSet) {
               ships.add(ship);
           }

        }
        return ships;
    }


    public Map<String, Object> toDTO(){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("created", dateAndTimeOfCreation);
        dto.put("gamePlayers", getGamePlayerList());
        dto.put("scores", getScoresList());
        return  dto;
    }


    private List<Map<String, Object>> getScoresList() {
        return scores.stream().map(Score::toDTO).collect(Collectors.toList());
    }

    private List< Map<String, Object> > getGamePlayerList(){
        return   gamePlayers
                .stream()
                .map(gamePlayer -> gamePlayer.toDTO())
                .collect(Collectors.toList());
    }

    public boolean isFull(){
        return gamePlayers.size() == 2;
    }


}
