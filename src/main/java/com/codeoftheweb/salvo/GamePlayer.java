package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // genera un nuevo valor de la variable id cada vez que se instancia a Player
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    private LocalDateTime playerJoinDate;

    public GamePlayer(){
        playerJoinDate = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }

    public GamePlayer(Player player, Game game, LocalDateTime date){
        this.player = player;
        this.game = game;
        this.playerJoinDate = date;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public Long getId(){
        return id;
    }

    public Game getGame(){
        return game;
    }

    public LocalDateTime getPlayerJoinDate(){
        return playerJoinDate;
    }



}
