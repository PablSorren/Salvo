package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.ZoneId;

//probar en http://localhost:8080/rest

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository players, GameRepository games, GamePlayerRepository gamePlayer) {
		return (args) -> {


			//SAVING NEW PLAYERS
			Player p1 = players.save(new Player("j.bauer@ctu.gov"));
			Player p2 = players.save(new Player("c.obrian@ctu.gov"));
			Player p3 = players.save(new Player("d.palmer@whitehouse.gov"));
			Player p4 = players.save(new Player("t.almeida@ctu.gov"));


			LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

			//SAVING NEW GAMES
			Game game1 = games.save(new Game(now));
			Game game2 = games.save(new Game(now));
			Game game3 = games.save(new Game(now));
			Game game4 = games.save(new Game(now));
			Game game5 = games.save(new Game(now));
			Game game6 = games.save(new Game(now));


			//SAVING NEW GAMES CONFIGS
			gamePlayer.save(new GamePlayer(p1,game1, LocalDateTime.of(2018, 2, 17, 3, 20, 15)));
			gamePlayer.save(new GamePlayer(p2,game1, LocalDateTime.of(2018, 2, 17, 3, 20, 15)));

			gamePlayer.save(new GamePlayer(p2,game2, LocalDateTime.of(2018, 2, 17, 4, 20, 15)));
			gamePlayer.save(new GamePlayer(p1,game2, LocalDateTime.of(2018, 2, 17, 4, 20, 15)));

			gamePlayer.save(new GamePlayer(p2,game3, LocalDateTime.of(2018, 2, 17, 5, 20, 15)));
			gamePlayer.save(new GamePlayer(p4,game3, LocalDateTime.of(2018, 2, 17, 5, 20, 15)));

			gamePlayer.save(new GamePlayer(p1,game4, LocalDateTime.of(2018, 2, 17, 6, 20, 15)));
			gamePlayer.save(new GamePlayer(p2,game4, LocalDateTime.of(2018, 2, 17, 6, 20, 15)));

			gamePlayer.save(new GamePlayer(p1,game5, LocalDateTime.of(2018, 2, 17, 7, 20, 15)));
			gamePlayer.save(new GamePlayer(p4,game5, LocalDateTime.of(2018, 2, 17, 7, 20, 15)));

			gamePlayer.save(new GamePlayer(p3,game6, LocalDateTime.of(2018, 2, 17, 8, 20, 15)));



		};
	}


}
