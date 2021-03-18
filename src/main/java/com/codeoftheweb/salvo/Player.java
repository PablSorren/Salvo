package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity // Indica que JPA construya una tabla para esta clase
public class Player {


   /*Indica que la variable id va a ser la primary key para la tabla,
   NUNCA va a generar 2 ids iguales
   y que TODAS las demas variables van a ser columnas de la tabla*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // genera un nuevo valor de la variable id cada vez que se instancia a Player
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    private String userName;


    public Player() {

    }

    public Player( String email) {

        this.userName = email;
    }

    public void setUserName(String email){

        this.userName = email;
    }
    public long getUserId(){
        return id;
    }

    public String getUserName()
    {
        return userName;
    }


    public void addGamePlayer(GamePlayer gamePlayer){
        //gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public Set<GamePlayer> getGamePlayers(){
        return gamePlayers;
    }

}
