/*
 * Copyright 2017 Ben-Hur Carlos Vieira Langoni Junior
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pbaioni.chesslib.pgn;

import static pbaioni.chesslib.pgn.PgnProperty.UTF8_BOM;
import static pbaioni.chesslib.pgn.PgnProperty.isProperty;
import static pbaioni.chesslib.pgn.PgnProperty.parsePgnProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import pbaioni.chesslib.game.Event;
import pbaioni.chesslib.game.Game;
import pbaioni.chesslib.game.GameFactory;
import pbaioni.chesslib.game.GameResult;
import pbaioni.chesslib.game.GenericPlayer;
import pbaioni.chesslib.game.Player;
import pbaioni.chesslib.game.Round;
import pbaioni.chesslib.game.Termination;
import pbaioni.chesslib.game.TimeControl;

/**
 * The type Game Loader.
 * <p>
 * The game loader permits loading a single PGN game
 */
public class GameLoader {
	
	private static Event event;
	private static Round round;
	private static Game game;

	public static List<Game> loadGames(Iterator<String> iterator) throws Exception {

		List<Game> games = new ArrayList<Game>();
		initGame();
		
		while (iterator.hasNext()) {
			String line = iterator.next();
			try {
				line = line.trim();
				if (line.startsWith(UTF8_BOM)) {
					line = line.substring(1);
				}
				if (isProperty(line)) {
					PgnProperty p = parsePgnProperty(line);
					if (p != null) {
						String tag = p.name.toLowerCase().trim();
						// begin
						switch (tag) {
						case "event":

							event.setName(p.value);

							break;
						case "site":

							event.setSite(p.value);

							break;
						case "date":

							event.setStartDate(p.value);

							break;
						case "round":

							round.setNumber(Integer.parseInt(p.value));

							break;
						case "white": {

							game.getWhitePlayer().setName(p.value);

							break;
						}
						case "black": {

							game.getBlackPlayer().setName(p.value);

							break;
						}
						case "result":

							GameResult r = GameResult.fromNotation(p.value);
							game.setResult(r);

							break;
						case "plycount":

							game.setPlyCount(p.value);

							break;
						case "termination":

							try {
								game.setTermination(Termination.fromValue(p.value.toUpperCase()));
							} catch (Exception e1) {
								game.setTermination(Termination.UNTERMINATED);
							}

							break;
						case "timecontrol":

							try {
								event.setTimeControl(TimeControl.parseFromString(p.value.toUpperCase()));
							} catch (Exception e1) {
								throw new PgnException(
										"Error parsing TimeControl Tag [" + (round != null ? round.getNumber() : 1)
												+ ", " + event.getName() + "]: " + e1.getMessage());
							}

							break;
						case "annotator":

							game.setAnnotator(p.value);

							break;
						case "fen":

							game.setFen(p.value);

							break;
						case "eco":

							game.setEco(p.value);

							break;
						case "opening":

							game.setOpening(p.value);

							break;
						case "variation":

							game.setVariation(p.value);

							break;
						case "whiteelo":

							try {
								game.getWhitePlayer().setElo(Integer.parseInt(p.value));
							} catch (NumberFormatException e) {

							}

							break;
						case "blackelo":

							try {
								game.getBlackPlayer().setElo(Integer.parseInt(p.value));
							} catch (NumberFormatException e) {

							}

							break;
						default:

							if (game.getProperty() == null) {
								game.setProperty(new HashMap<String, String>());
							}
							game.getProperty().put(p.name, p.value);

							break;
						}
					}
				} else if (!line.trim().equals("")) {
					
					game.appendMoveText(line + " ");

					if (line.endsWith("1-0") || line.endsWith("0-1") || line.endsWith("1/2-1/2")
							|| line.endsWith("*")) {

						//finalizing game loading
						game.loadMoveText();
						games.add(game);
						
						//start a new cycle
						initGame();

					}
				}

			} catch (Exception e) {
				String name = "";
				int r = 0;
				try {
					r = round.getNumber();
					name = event.getName();
				} catch (Exception e2) {

				}
				throw new PgnException("Error parsing PGN[" + r + ", " + name + "]: ", e);
			}

		}

		return games;
	}
	
	private static void initGame() {
		
		// initializing game
		event = new Event();
		round = new Round(event);
		game = GameFactory.newGame(UUID.randomUUID().toString(), round);
		Player whitePlayer = new GenericPlayer();
		Player blackPlayer = new GenericPlayer();
		game.setWhitePlayer(whitePlayer);
		game.setBlackPlayer(blackPlayer);

	}

}