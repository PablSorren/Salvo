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
	public CommandLineRunner initData(PlayerRepository players, GameRepository games, GamePlayerRepository gamePlayer, ShipRepository ships, SalvoRepository salvo) {
		return (args) -> {


			//SAVING NEW PLAYERS
			Player jbauer = players.save(new Player("j.bauer@ctu.gov"));
			Player cobrian = players.save(new Player("c.obrian@ctu.gov"));
			Player dpalmer = players.save(new Player("d.palmer@whitehouse.gov"));
			Player talmeida = players.save(new Player("t.almeida@ctu.gov"));


			LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

			//SAVING NEW GAMES
			Game game1 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 3, 20, 15)));
			Game game2 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 4, 20, 15)));
			Game game3 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 5, 20, 15)));
			Game game4 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 6, 20, 15)));
			Game game5 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 7, 20, 15)));
			Game game6 = games.save(new Game(LocalDateTime.of(2018, 2, 17, 8, 20, 15)));


			//SAVING NEW GAMES CONFIGS
			GamePlayer gp1 = gamePlayer.save(new GamePlayer(jbauer,game1));
			GamePlayer gp2 = gamePlayer.save(new GamePlayer(cobrian,game1));

			GamePlayer gp3 =gamePlayer.save(new GamePlayer(cobrian,game2));
			GamePlayer gp4 =gamePlayer.save(new GamePlayer(jbauer,game2));

			GamePlayer gp5 =gamePlayer.save(new GamePlayer(cobrian,game3));
			GamePlayer gp6 =gamePlayer.save(new GamePlayer(talmeida,game3));

			GamePlayer gp7 =gamePlayer.save(new GamePlayer(jbauer,game4));
			GamePlayer gp8 =gamePlayer.save(new GamePlayer(cobrian,game4));

			GamePlayer gp9 =gamePlayer.save(new GamePlayer(jbauer,game5));
			GamePlayer gp10 =gamePlayer.save(new GamePlayer(talmeida,game5));

			GamePlayer gp11 =gamePlayer.save(new GamePlayer(dpalmer,game6));

			//SAVIN SHIPS

			//GAME #1
			Ship s1 = ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("H2", "H3","H4")), gp1));
			Ship s2 = ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("E1", "F1","G1")), gp1));
			Ship s3 = ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("B4", "B5")), gp1));

			Ship s4 = ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp2));
			Ship s5 = ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("F1", "F2")), gp2));

			//GAME #2
			Ship s6 = ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp4));
			Ship s7 = ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("C6", "C7")), gp4));

			Ship s8 = ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("A2", "A3", "A4")), gp3));
			Ship s9 = ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("G6", "H6")), gp3));

			//GAME #3
			Ship s10 = ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp5));
			Ship s11 = ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("C6", "C7")), gp5));

			Ship s12 = ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("A2", "A3", "A4")), gp6));
			Ship s13 = ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("G6", "H6")), gp6));

			//SALVOES

			salvo.save(new Salvo(gp1, 1, new ArrayList<>(Arrays.asList("B5", "C5", "F1")) ) );
			/*salvo.save(new Salvo(gp1, 2, new ArrayList<>(Arrays.asList("F2", "D5")) ) );

			salvo.save(new Salvo(gp2, 1, new ArrayList<>(Arrays.asList("B4", "B5", "B6")) ) );
			salvo.save(new Salvo(gp2, 2, new ArrayList<>(Arrays.asList("E1","H3", "A2")) ) );


			salvo.save(new Salvo(gp3, 1, new ArrayList<>(Arrays.asList("B5", "D5", "C7")) ) );
			salvo.save(new Salvo(gp3, 2, new ArrayList<>(Arrays.asList("C5", "C6")) ) );

			salvo.save(new Salvo(gp4, 1, new ArrayList<>(Arrays.asList("A2", "A4", "G6")) ) );
			salvo.save(new Salvo(gp4, 2, new ArrayList<>(Arrays.asList("A3", "H6")) ) );
*/

		};
	}


}
