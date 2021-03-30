package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import static java.util.stream.Collectors.toList;

@Entity // Indica que JPA construya una tabla para esta clase
public class Player {


    /*Indica que la variable id va a ser la primary key para la tabla,
    NUNCA va a generar 2 ids iguales
    y que TODAS las demas variables van a ser columnas de la tabla*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    // genera un nuevo valor de la variable id cada vez que se instancia a Player
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;


    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores;

    private String email;


    public Player() {
        gamePlayers = new HashSet<>();
        scores = new HashSet<>();
    }

    public Player(String email) {

        this.email = email;
    }


    public long getUserId() {
        return id;
    }

    public String getEmail() {
        return email;
    }


    public void addGamePlayer(GamePlayer gamePlayer) {

        gamePlayers.add(gamePlayer);
    }

    @JsonIgnore //evita recursividad
    public List<Game> getGames() {
        return gamePlayers.stream().map(g -> g.getGame()).collect(toList());
    }


    public Map<String, Object> toDTO(){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("email" , email);
        return dto;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void addScore(Score score) {
        scores.add(score);
    }


}