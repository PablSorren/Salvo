package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository players, GameRepository games, GamePlayerRepository gamePlayer, ShipRepository ships) {
		return (args) -> {


			//SAVING NEW PLAYERS
			Player p1 = players.save(new Player("j.bauer@ctu.gov"));
			Player p2 = players.save(new Player("c.obrian@ctu.gov"));
			Player p3 = players.save(new Player("d.palmer@whitehouse.gov"));
			Player p4 = players.save(new Player("t.almeida@ctu.gov"));


			LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

			//SAVING NEW GAMES
			Game game1 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 3, 20, 15)));
			Game game2 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 4, 20, 15)));
			Game game3 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 5, 20, 15)));
			Game game4 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 6, 20, 15)));
			Game game5 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 7, 20, 15)));
			Game game6 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 8, 20, 15)));


			//SAVING NEW GAMES CONFIGS
			GamePlayer gp1 = gamePlayer.save(new GamePlayer(p1,game1));
			GamePlayer gp2 = gamePlayer.save(new GamePlayer(p2,game1));

			GamePlayer gp3 =gamePlayer.save(new GamePlayer(p2,game2));
			GamePlayer gp4 =gamePlayer.save(new GamePlayer(p1,game2));

			GamePlayer gp5 =gamePlayer.save(new GamePlayer(p2,game3));
			GamePlayer gp6 =gamePlayer.save(new GamePlayer(p4,game3));

			GamePlayer gp7 =gamePlayer.save(new GamePlayer(p1,game4));
			GamePlayer gp8 =gamePlayer.save(new GamePlayer(p2,game4));

			GamePlayer gp9 =gamePlayer.save(new GamePlayer(p1,game5));
			GamePlayer gp10 =gamePlayer.save(new GamePlayer(p4,game5));

			GamePlayer gp11 =gamePlayer.save(new GamePlayer(p3,game6));

			//SAVIN SHIPS

			Ship s1 = ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("H2", "H3","H4")), gp1));


			Ship s2 = ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("E1", "F1","G1")), gp1));


			Ship s3 = ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("B4", "B5")), gp1));

			Ship s4 = ships.save(new Ship(ShipType.Carrier,new ArrayList<String>(Arrays.asList("A2","A3","A4", "A5", "A6")), gp3));


		};
	}


}
