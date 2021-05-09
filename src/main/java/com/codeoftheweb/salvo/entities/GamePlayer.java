package com.codeoftheweb.salvo.entities;

import com.codeoftheweb.salvo.Util;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.*;
import java.util.stream.Collectors;


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

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships;


    //cascade = CascadeType.ALL automatizael relacionar padre-hijo, sin esto, tendria un hijo (salvoes) sin el padre(gameplayer)
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvoes;


    private LocalDateTime playerJoinDate;



    public GamePlayer(){

        playerJoinDate = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        this.ships = new LinkedHashSet<>();
        this.salvoes = new  LinkedHashSet<>();
    }

    public GamePlayer(Player player, Game game){
        this();
        this.player = player;
        this.game = game;

    }


    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getPlayerId(){
        return player.getId();
    }

    public String getUserName(){
        return player.getUsername();
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




    public void addShip(Ship ship){
        ships.add(ship);
    }


    public boolean shipsPlaced() {
        return ships.size() != 0;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }


    public void addSalvo(Salvo salvo) {
        salvoes.add(salvo);
    }


    public Map<String, Object> toDTO(){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("player" ,player.toDTO());
        dto.put("joinDate", playerJoinDate);
        return dto;
    }

    public Map<String, Object> gameView(){

        Map<String, Object> gameViewMap = game.toDTO();
        gameViewMap.put("ships", ships.stream().map(Ship::toDTO));
        gameViewMap.put("salvoes",getSalvoesList());
        return gameViewMap;
    }


    private List<Map<String,Object>> getSalvoesList(){
        return game.getGamePlayers()
                .stream()
                .flatMap(gp -> gp.getSalvoes().stream())
                .map(Salvo::toDTO)
                .collect(Collectors.toList());
    }


    public Score getScore(){
        return player.getScore(game);
    }

    public  GamePlayer getOpponent(){

        Game currentGame = this.getGame();

        GamePlayer opponent = currentGame
                .getGamePlayers()
                .stream()
                .filter(gp -> gp.getId() != this.getId())
                .findFirst()
                .orElse(null)
                ;
        return opponent;
    }

    public List<Map<String, Object>> listOfHitsAndMisses(){


        List<Map<String, Object>> listOfHitsAndMisses = new ArrayList<>();

        int carrier = 0, battleship = 0, submarine = 0, destroyer = 0, patrolboat = 0;

        List<String> carrierLocations = shipLocations(ShipType.CARRIER);
        List<String> battleshipLocations = shipLocations(ShipType.BATTLESHIP);
        List<String> submarineLocations = shipLocations(ShipType.SUBMARINE);
        List<String> destroyerLocations = shipLocations(ShipType.DESTROYER);
        List<String> patrolboatLocations = shipLocations(ShipType.PATROL_BOAT);

        for(Salvo salvo : this.getOpponent().getSalvoes()) {

            Map<String, Object> hitMapPerTurn = new LinkedHashMap<>();
            List<String> hitLocations = new ArrayList<>();

            int carrierHits = 0, battleshipHits = 0, submarineHits = 0, destroyerHits = 0, patrolboatHits = 0;
            int missedShots = salvo.getSalvoLocations().size();

            Map<String, Object> damagesPerTurn = new LinkedHashMap<>();

            for(String location : salvo.getSalvoLocations()) {

                if(carrierLocations.contains(location)) {
                    carrier++;
                    carrierHits++;
                    missedShots--;
                    hitLocations.add(location);
                }

                if(battleshipLocations.contains(location)) {
                    battleship++;
                    battleshipHits++;
                    missedShots--;
                    hitLocations.add(location);
                }

                if(submarineLocations.contains(location)) {
                    submarine++;
                    submarineHits++;
                    missedShots--;
                    hitLocations.add(location);
                }

                if(destroyerLocations.contains(location)) {
                    destroyer++;
                    destroyerHits++;
                    missedShots--;
                    hitLocations.add(location);
                }

                if(patrolboatLocations.contains(location)) {
                    patrolboat++;
                    patrolboatHits++;
                    missedShots--;
                    hitLocations.add(location);
                }

            }

            damagesPerTurn.put("carrierHits", carrierHits);
            damagesPerTurn.put("battleshipHits", battleshipHits);
            damagesPerTurn.put("submarineHits", submarineHits);
            damagesPerTurn.put("destroyerHits", destroyerHits);
            damagesPerTurn.put("patrolboatHits", patrolboatHits);
            damagesPerTurn.put("carrier", carrier);
            damagesPerTurn.put("battleship", battleship);
            damagesPerTurn.put("submarine", submarine);
            damagesPerTurn.put("destroyer", destroyer);
            damagesPerTurn.put("patrolboat", patrolboat);


            hitMapPerTurn.put("turn", salvo.getTurn());
            hitMapPerTurn.put("hitLocations",hitLocations);
            hitMapPerTurn.put("damages",damagesPerTurn);
            hitMapPerTurn.put("missed", missedShots);

            listOfHitsAndMisses.add(hitMapPerTurn);
        }

        return listOfHitsAndMisses;
    }


    private List<String> shipLocations(ShipType shipType) {

        Optional<Ship> shipLocations =  ships
                                        .stream()
                                        .filter(s -> s.getType() == shipType )
                                        .findFirst();

        if(!shipLocations.isPresent()) {
            return new ArrayList<>();
        } else {
            return shipLocations.get().getLocations();
        }
    }



    public Boolean checkIfAllShipsSunk() {

        GamePlayer opponent = getOpponent();

        if(!ships.isEmpty() && !opponent.getSalvoes().isEmpty()){

            return opponent
                    .getSalvoes()
                    .stream()
                    .flatMap(salvo -> salvo.getSalvoLocations().stream())
                    .collect(Collectors.toList())
                    .containsAll(ships
                            .stream()
                            .flatMap(ship -> ship.getLocations().stream())
                            .collect(Collectors.toList())
                    );
        }
        return false;
    }



}
