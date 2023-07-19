// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package swen221.cards.core;

import java.util.Map;

import swen221.cards.core.Player.Direction;
import swen221.cards.variations.ClassicWhist;

/**
 * Represents a single card in the game.
 *
 * @author David J. Pearce
 *
 */
public class Card implements Comparable<Card>, Cloneable {

	/**
	 * Represents a card suit.
	 *
	 * @author David J. Pearce
	 *
	 */
	public enum Suit {
		/**
		 * The suit of hearts.
		 */
		HEARTS,
		/**
		 * The suit of clubs.
		 */
		CLUBS,
		/**
		 * The suit of diamonds.
		 */
		DIAMONDS,
		/**
		 * The suit of spades.
		 */
		SPADES;
	}

	/**
	 * Represents the different card "numbers".
	 *
	 * @author David J. Pearce
	 *
	 */
	public enum Rank {
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING,
		ACE;
	}

	// =======================================================
	// Card stuff
	// =======================================================

	private Suit suit; // HEARTS, CLUBS, DIAMONDS, SPADES
	private Rank rank; // 2 <= number <= 14 (ACE)

	/**
	 * Construct a card in the given suit, with a given number
	 *
	 * @param suit
	 *            --- between 0 (HEARTS) and 3 (SPADES)
	 * @param number
	 *            --- between 2 and 14 (ACE)
	 */
	public Card(Suit suit, Rank number) {
		this.suit = suit;
		this.rank = number;
	}

	/**
	 * Get the suit of this card, between 0 (HEARTS) and 3 (SPADES).
	 *
	 * @return The suite of this card.
	 */
	public Suit suit() {
		return suit;
	}

	/**
	 * Get the number of this card, between 2 and 14 (ACE).
	 *
	 * @return The rank of this card.
	 */
	public Rank rank() {
		return rank;
	}

	private static String[] suits = { "Hearts","Clubs","Diamonds","Spades"};
	private static String[] ranks = { "2 of ", "3 of ", "4 of ",
			"5 of ", "6 of ", "7 of ", "8 of ", "9 of ", "10 of ", "Jack of ",
			"Queen of ", "King of ", "Ace of " };

	@Override
	public String toString() {
		return ranks[rank.ordinal()] + suits[suit.ordinal()];
	}
	
	/**
	 * Equals method to check other cards
	 */
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o instanceof Card) {
			Card other = (Card)o;
			if(this.suit == other.suit && this.rank == other.rank) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Hash code method to produce unique int value for a card
	 */
	@Override
	public int hashCode() {
		int prime = 41;
		int result = 1;
		result = result * prime + suit.hashCode();
		result = result * prime + rank.hashCode();
		return result;
	}

	
	/**
	 * Compare to method that compares two cards
	 */
	@Override
	public int compareTo(Card o) {
		//  check the suits
		if(this.suit.ordinal() < o.suit.ordinal()) return -1;
		else if(this.suit.ordinal() > o.suit.ordinal()) return 1;
		else {
			// they have the same suit, then check the number
			if(this.rank.ordinal() < o.rank.ordinal()) return -1;
			else if(this.rank.ordinal() > o.rank.ordinal()) return 1;
			else return 0;
		}
	}
	
	/**
	 * Clones a card object
	 */
	@Override
	public Card clone(){
		try {
			return (Card) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
