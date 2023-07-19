// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package swen221.cards.variations;

import java.util.HashSet;
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
 * An implementation of the "knock-out" rules of Whist.
 *
 * @author David J. Pearce
 *
 */
public class KnockOutWhist extends AbstractCardGame implements Cloneable {
	private int hand = 13;

	/**
	 * Construct a new game of "knock out" Whist.
	 */
	public KnockOutWhist() {

	}
	
	@Override
	public CardGame clone(){
		// This clone method used the default clone method found in
		// java.lang.Object. This creates a shallow copy of the card game. For
		// Part 3 of the assignment, you need to re-implement this to perform a
		// deep clone.
		
		KnockOutWhist clonedGame = new KnockOutWhist();
		
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
		clonedGame.hand = this.hand;
		return (CardGame) clonedGame;
		
	}

	@Override
	public String getName() {
		return "Knock-Out Whist";
	}

	@Override
	public boolean isGameFinished() {
		return hand == 0;
	}

	@Override
	public void deal(List<Card> deck) {
		currentTrick = null;
		for (Player.Direction d : Player.Direction.values()) {
			players.get(d).getHand().clear();
		}
		Player.Direction d = Player.Direction.NORTH;
		for (int i = 0; i < hand * 4; ++i) {
			Card card = deck.get(i);
			players.get(d).getHand().add(card);
			d = d.next();
		}
	}

	@Override
	public void endHand() {
		super.endHand();
		hand = hand - 1;
	}
}
