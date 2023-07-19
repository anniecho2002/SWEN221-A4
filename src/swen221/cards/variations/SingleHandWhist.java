// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package swen221.cards.variations;

import java.util.List;
import java.util.Map;
import java.util.Set;

import swen221.cards.core.Card;
import swen221.cards.core.CardGame;
import swen221.cards.core.IllegalMove;
import swen221.cards.core.Player;
import swen221.cards.core.Trick;
import swen221.cards.core.Player.Direction;
import swen221.cards.util.AbstractCardGame;

/**
 * A simple variation of Whist where only a single hand is played.
 *
 * @author David J. Pearce
 *
 */
public class SingleHandWhist extends AbstractCardGame implements Cloneable {

	/**
	 * Construct a new game of "single hand" Whist.
	 */
	public SingleHandWhist() {

	}
	
	@Override
	public CardGame clone(){
		// This clone method used the default clone method found in
		// java.lang.Object. This creates a shallow copy of the card game. For
		// Part 3 of the assignment, you need to re-implement this to perform a
		// deep clone.
		
		SingleHandWhist clonedGame = new SingleHandWhist();
		
		clonedGame.players.put(Direction.NORTH, this.getPlayer(Direction.NORTH).clone());
		clonedGame.players.put(Direction.SOUTH, this.getPlayer(Direction.SOUTH).clone());
		clonedGame.players.put(Direction.EAST, this.getPlayer(Direction.EAST).clone());
		clonedGame.players.put(Direction.WEST, this.getPlayer(Direction.WEST).clone());
		
		Map<Player.Direction, Integer> copyTricks = this.getTricksWon();
		for(Map.Entry<Player.Direction, Integer> entry : copyTricks.entrySet()) {
			clonedGame.tricks.put(entry.getKey(), entry.getValue());
		}
		
		Map<Player.Direction, Integer> copyScores = this.getOverallScores();
		for(Map.Entry<Player.Direction, Integer> entry : copyScores.entrySet()) {
			clonedGame.scores.put(entry.getKey(), entry.getValue());
		}
		if(this.getTrick() != null) {
			clonedGame.currentTrick = this.getTrick().clone();
		}
		clonedGame.trumps = this.trumps;
		return (CardGame) clonedGame;
		
	}

	@Override
	public String getName() {
		return "Single Hand Whist";
	}

	@Override
	public boolean isGameFinished() {
		for (Player.Direction d : Player.Direction.values()) {
			if (scores.get(d) == 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void deal(List<Card> deck) {
		currentTrick = null;
		for (Player.Direction d : Player.Direction.values()) {
			players.get(d).getHand().clear();
		}
		Player.Direction d = Player.Direction.NORTH;
		for (int i = 0; i < deck.size(); ++i) {
			Card card = deck.get(i);
			players.get(d).getHand().add(card);
			d = d.next();
		}
	}
}
