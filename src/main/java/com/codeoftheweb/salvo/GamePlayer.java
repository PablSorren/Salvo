package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


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

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;


    private LocalDateTime playerJoinDate;

    public GamePlayer(){

        playerJoinDate = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        this.ships = new HashSet<>();
    }

    public GamePlayer(Player player, Game game){
        this();
        this.player = player;
        this.game = game;

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

    public long getPlayerId(){
        return player.getUserId();
    }

    public String getUserName(){
        return player.getUserName();
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


    public Set<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }


    public Map<String, Object> toDTO(){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gamePlayer_id", id);
        dto.put("player" ,player.toDTO());
        dto.put("join_Date", playerJoinDate);
        return dto;
    }

    public Map<String, Object> gameView(){

        Map<String, Object> gameViewMap = game.toDTO();
        gameViewMap.put("ships", ships.stream().map(Ship::toDTO));
        return gameViewMap;
    }

}
