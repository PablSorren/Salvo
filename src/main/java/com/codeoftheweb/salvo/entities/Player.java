package com.codeoftheweb.salvo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity // Indica que JPA construya una tabla para esta clase
public class Player {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    // genera un nuevo valor de la variable id cada vez que se instancia a Player
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;


    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores;

    private String username;
    private String password;


    public Player() {
        gamePlayers = new HashSet<>();
        scores = new HashSet<>();
    }

    public Player(String username, String password) {

        this.username = username;
        this.password = password;
    }


    public long getId() {
        return id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @JsonIgnore //evita recursividad
    public List<Game> getGames() {
        return gamePlayers.stream().map(g -> g.getGame()).collect(toList());
    }


    public Map<String, Object> toDTO(){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("email" , username);
        return dto;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void addScore(Score score) {
        scores.add(score);
    }

    public Score getScore(Game game){
         return scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }

    public double getTotalScore(){
        return scores
                .stream()
                .filter(s -> Double.compare(s.getScore(),-1) !=0)
                .mapToDouble(Score::getScore)
                .sum();
    }

    public long getWinsCount(){
        return getResult(1);
    }

    public long getLosesCount(){
        return getResult(0);
    }

    public long getTiesCount(){
        return getResult(0.5);
    }


    private long getResult(double s) {
        return scores.stream().filter(score -> Double.compare(score.getScore(), s) == 0).count();
    }

    public Map<String, Object> scoreToDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("name", username);
        dto.put("total", getTotalScore());
        dto.put("won", getWinsCount());
        dto.put("lost", getLosesCount());
        dto.put("tied", getTiesCount());
        return dto;
    }
}