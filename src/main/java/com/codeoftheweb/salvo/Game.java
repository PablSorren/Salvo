package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    // instanciar con Game game1 = new Game(LocalDateTime.now());
    public Game(LocalDateTime time) {
        this.dateAndTimeOfCreation = time;
    }

    public long getGameId() {
        return id;
    }

    public LocalDateTime getDateAndTimeOfCreation(){
        return dateAndTimeOfCreation;
    }


    public void addGamePlayer(GamePlayer gamePlayer){

        gamePlayers.add(gamePlayer);
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
        return  dto;
    }


    private List< Map<String, Object> > getGamePlayerList(){
        return   gamePlayers
                .stream()
                .map(gamePlayer -> gamePlayer.toDTO())
                .collect(Collectors.toList());
    }

    public Set<Score> getScores(){
        return scores;
    }

    public void addScore(Score score){
        scores.add(score);
    }


}
