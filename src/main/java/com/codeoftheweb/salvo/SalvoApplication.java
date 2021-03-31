package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entities.*;
import com.codeoftheweb.salvo.repositories.*;
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
	public CommandLineRunner initData(PlayerRepository players, GameRepository games, GamePlayerRepository gamePlayer, ShipRepository ships, SalvoRepository salvo, ScoreRepository score) {
		return (args) -> {


			//PLAYERS
			Player jbauer = players.save(new Player("j.bauer@ctu.gov"));
			Player cobrian = players.save(new Player("c.obrian@ctu.gov"));
			Player kbauer = players.save(new Player("kim_bauer@gmail.com"));
			Player talmeida = players.save(new Player("t.almeida@ctu.gov"));


			LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

			//GAMES
			Game game1 = games.save(new Game(now));
			Game game2 = games.save(new Game(now));
			Game game3 = games.save(new Game(now));
			Game game4 = games.save(new Game(now));
			Game game5 = games.save(new Game(now));
			Game game6 = games.save(new Game(now));
			Game game7 = games.save(new Game(now));
			Game game8 = games.save(new Game(now));


			//GAME PLAYERS
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

			GamePlayer gp11 =gamePlayer.save(new GamePlayer(kbauer,game6));

			GamePlayer gp12 = gamePlayer.save(new GamePlayer(talmeida,game7));

			GamePlayer gp13 = gamePlayer.save(new GamePlayer(kbauer,game8));
			GamePlayer gp14 = gamePlayer.save(new GamePlayer(talmeida,game8));


			//SAVIN SHIPS

			//GAME #1
			 ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("H2", "H3","H4")), gp1));
			 ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("E1", "F1","G1")), gp1));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("B4", "B5")), gp1));

			 ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp2));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("F1", "F2")), gp2));

			//GAME #2
			 ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp4));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("C6", "C7")), gp4));

			 ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("A2", "A3", "A4")), gp3));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("G6", "H6")), gp3));

			//GAME #3
			 ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp5));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("C6", "C7")), gp5));

			 ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("A2", "A3", "A4")), gp6));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("G6", "H6")), gp6));

			//GAME #4
			 ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp8));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("C6", "C7")), gp8));

			 ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("A2", "A3", "A4")), gp7));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("G6", "H6")), gp7));

			//GAME #5
			 ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp10));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("C6", "C7")), gp10));

			 ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("A2", "A3", "A4")), gp9));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("G6", "H6")), gp9));

			//GAME #6
			 ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp11));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("C6", "C7")), gp11));

			//GAME #8
			 ships.save(new Ship(ShipType.Destroyer,new ArrayList<String>(Arrays.asList("B5", "C5", "D5")), gp13));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("C6", "C7")), gp13));

			 ships.save(new Ship(ShipType.Submarine,new ArrayList<String>(Arrays.asList("A2", "A3", "A4")), gp14));
			 ships.save(new Ship(ShipType.Patrol_Boat,new ArrayList<String>(Arrays.asList("G6", "H6")), gp14));


			//SALVOES
				//game 1 - turn 1
			salvo.save(new Salvo(gp1, 1, new ArrayList<>(Arrays.asList("B5", "C5", "F1")) ) );
			salvo.save(new Salvo(gp2, 1, new ArrayList<>(Arrays.asList("B4", "B5", "B6")) ) );
				//game 1 - turn 2
			salvo.save(new Salvo(gp1, 2, new ArrayList<>(Arrays.asList("F2", "D5")) ) );
			salvo.save(new Salvo(gp2, 2, new ArrayList<>(Arrays.asList("E1","H3", "A2")) ) );
				//game 2 - turn 1
			salvo.save(new Salvo(gp3, 1, new ArrayList<>(Arrays.asList("B5", "D5", "C7")) ) );
			salvo.save(new Salvo(gp4, 1, new ArrayList<>(Arrays.asList("A2", "A4", "G6")) ) );
				//game 2 - turn 2
			salvo.save(new Salvo(gp3, 2, new ArrayList<>(Arrays.asList("C5", "C6")) ) );
			salvo.save(new Salvo(gp4, 2, new ArrayList<>(Arrays.asList("A3", "H6")) ) );
				//game 3 - turn 1
			salvo.save(new Salvo(gp5, 1, new ArrayList<>(Arrays.asList("G6", "H6", "A4")) ) );
			salvo.save(new Salvo(gp6, 1, new ArrayList<>(Arrays.asList("H1", "H2", "H3")) ) );
				//game 3 - turn 2
			salvo.save(new Salvo(gp5, 2, new ArrayList<>(Arrays.asList("A2", "A3", "D8")) ) );
			salvo.save(new Salvo(gp6, 2, new ArrayList<>(Arrays.asList("E1", "F2", "G3")) ) );
				//game 4 - turn 1
			salvo.save(new Salvo(gp7, 1, new ArrayList<>(Arrays.asList("B5", "C6", "H1")) ) );
			salvo.save(new Salvo(gp8, 1, new ArrayList<>(Arrays.asList("A3", "A4", "F7")) ) );
				//game 4 - turn 2
			salvo.save(new Salvo(gp7, 2, new ArrayList<>(Arrays.asList("C5", "C7", "D5")) ) );
			salvo.save(new Salvo(gp8, 2, new ArrayList<>(Arrays.asList("A2", "G6", "H6")) ) );
				//game 5 - turn 1
			salvo.save(new Salvo(gp9, 1, new ArrayList<>(Arrays.asList("A1", "A2", "A3")) ) );
			salvo.save(new Salvo(gp10, 1, new ArrayList<>(Arrays.asList("B5", "B6", "C7")) ) );
				//game 5 - turn 2
			salvo.save(new Salvo(gp9, 2, new ArrayList<>(Arrays.asList("C6", "D6", "E6")) ) );
			salvo.save(new Salvo(gp10, 2, new ArrayList<>(Arrays.asList("G6", "G7", "G8")) ) );
				//game 5 - turn 3
			salvo.save(new Salvo(gp10, 3, new ArrayList<>(Arrays.asList("H1", "H8")) ) );

			//SCORES
			double win = 1;
			double lose = 0;
			double tie = 0.5;

			//GAME 1
			score.save(new Score(jbauer, game1, win));
			score.save(new Score(cobrian, game1, lose));

			//GAME 2
			score.save(new Score(jbauer, game2, tie));
			score.save(new Score(cobrian, game2, tie));

			//GAME 3
			score.save(new Score(cobrian, game3, win));
			score.save(new Score(talmeida, game3, lose));

			//GAME 4
			score.save(new Score(cobrian, game4, tie));
			score.save(new Score(jbauer, game4, tie));




		};
	}


}
