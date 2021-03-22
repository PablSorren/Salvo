package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;


    private LocalDateTime dateAndTimeOfCreation;

    public Game() {

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

        return gamePlayers.stream().map(p -> p.getPlayer()).collect(toList());
    }

}
