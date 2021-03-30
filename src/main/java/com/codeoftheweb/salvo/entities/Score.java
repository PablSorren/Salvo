package com.codeoftheweb.salvo.entities;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

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

    private double score;

    private LocalDate finishDate;

    public Score() {

    }


    public Score(Player player, Game game, double score) {
        this.player = player;
        this.game = game;
        this.score = score;

        finishDate = LocalDate.now();
    }

    public long getId() {
        return id;
    }


    public Player getPlayer() {
        return player;
    }


    public Game getGame() {
        return game;
    }


    public double getScore() {
        return score;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("score", score);
        dto.put("player",player.getUserId());
        dto.put("finishDate", finishDate);
        return dto;
    }

}
