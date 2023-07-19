// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package swen221.cards.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import swen221.cards.core.Card;
import swen221.cards.core.Card.Suit;
import swen221.cards.core.Hand;
import swen221.cards.core.Player;
import swen221.cards.core.Player.Direction;
import swen221.cards.core.Trick;

/**
 * Implements a simple computer player who plays the highest card available when
 * the trick can still be won, otherwise discards the lowest card available. In
 * the special case that the player must win the trick (i.e. this is the last
 * card in the trick), then the player conservatively plays the least card
 * needed to win.
 *
 * @author David J. Pearce
 *
 */
public class SimpleComputerPlayer extends AbstractComputerPlayer {

	/**
	 * Construct a new (simple) computer player with the given player information.
	 *
	 * @param player Key player information.
	 */
	public SimpleComputerPlayer(Player player) {
		super(player);
	}

	@Override
	public Card getNextCard(Trick trick) {

		Hand playerHand = player.getHand();
		List<Card> cardsPlayed = trick.getCardsPlayed();
		Suit suitToFollow = null;
		
		if (cardsPlayed.size() == 3) {
			// Last player as there are 3 played cards already
			suitToFollow = cardsPlayed.get(0).suit(); // same suit as leader
			Card highest = highestCard(trick.getTrumps(), suitToFollow, cardsPlayed);
			return last(highest, playerHand, trick.getTrumps(), suitToFollow);
		} 
		else if (!cardsPlayed.isEmpty()) { 
			// Second or third player
			suitToFollow = cardsPlayed.get(0).suit(); // same suit as leader
			Card highest = highestCard(trick.getTrumps(), suitToFollow, cardsPlayed);
			return toPlay(highest, playerHand, trick.getTrumps(), suitToFollow);
		} 
		else { 
			// You are going first
			return leader(playerHand, trick.getTrumps());
		}
	}
	
	
	/**
	 * Returns the list of suits, excluding the trump suit
	 * @param trump
	 * @return
	 */
	List<Suit> withoutTrump(Suit trump){
		List<Suit> allSuits = new ArrayList<Suit>();
		allSuits.add(Card.Suit.CLUBS);
		allSuits.add(Card.Suit.HEARTS);
		allSuits.add(Card.Suit.DIAMONDS);
		allSuits.add(Card.Suit.SPADES);
		allSuits.remove(trump);
		return allSuits;
	}
	
	/**
	 * Return our own highest card of the suit
	 * 
	 * @param s suit
	 * @param h hand
	 * @return
	 */
	private Card ownHighest(Suit s, Hand h) {
		Set<Card> suitMatches = h.matches(s);
		Card ownHighest = null;

		if (!suitMatches.isEmpty()) {
			for (Card c : suitMatches) {
				if (ownHighest == null) ownHighest = c;
				else {
					if (c.rank().ordinal() > ownHighest.rank().ordinal()) ownHighest = c;
				}
			}
		}
		return ownHighest;
	}

	
	/**
	 * Return our own lowest card of the suit
	 * 
	 * @param s suit
	 * @param h hand
	 * @return
	 */
	private Card ownLowest(Suit s, Hand h) {
		Set<Card> suitMatches = h.matches(s);
		Card ownLowest = null;

		if (!suitMatches.isEmpty()) {
			for (Card c : suitMatches) {
				if (ownLowest == null) ownLowest = c;
				else {
					if (c.rank().ordinal() < ownLowest.rank().ordinal()) ownLowest = c;
				}
			}
		}
		return ownLowest;
	}
	

	/**
	 * Leader function, 
	 * leader will play their biggest card
	 * 
	 * @param h Player's hand
	 * @param trump Trump for the trick
	 * @return the card to be played by the leader
	 */
	private Card leader(Hand h, Suit trump) {
		if (trump != null) {
			Set<Card> suitMatches = h.matches(trump);
			if (!suitMatches.isEmpty()) return ownHighest(trump, h);
			// if there is no trump, then return the highest card
		}

		List<Suit> allSuits = withoutTrump(trump);
		List<Card> allCards = new ArrayList<Card>();
		
		for (Suit s : allSuits) {
			Set<Card> suitMatches = h.matches(s);
			for (Card c : suitMatches) allCards.add(c);
		}

		// Sort the cards to find the highest card
		Collections.sort(allCards, new Comparator<Card>() {
			public int compare(Card c1, Card c2) {
				// check the rank first, because you get to choose the suit
				if (c1.rank().ordinal() > c2.rank().ordinal()) return 1;
				if (c1.rank().ordinal() < c2.rank().ordinal()) return -1;
				else {
					// if they have the same rank, then sort by suit
					if (c1.suit().ordinal() < c2.suit().ordinal()) return -1;
					if (c1.suit().ordinal() > c2.suit().ordinal()) return 1;
					return 0;
				}
			}
		});
		return allCards.get(allCards.size() - 1); // return the last card/highest card
	}

	/**
	 * Last player function,
	 * If you are able to win the trick, then play the lowest possible card (so you win)
	 * 
	 * @param highest Highest card
	 * @param h Player's hand
	 * @param trump Trump for the trick
	 * @param follow Suit to follow
	 * @return
	 */
	private Card last(Card highest, Hand h, Suit trump, Suit follow) {
		Set<Card> suitMatches = h.matches(follow);
		if (!suitMatches.isEmpty()) { 
			// If you have any cards that can follow suit
			if (highest.suit() == follow) {
				Card ownHighest = ownHighest(follow, h);
				if (ownHighest.rank().ordinal() > highest.rank().ordinal()) {
					for (Card c : suitMatches) {
						if (c.rank().ordinal() > highest.rank().ordinal() 
						&& c.rank().ordinal() < ownHighest.rank().ordinal()) ownHighest = c;
					}
					return ownHighest; // play the lowest possible card for you to still win
				} 
				else return ownLowest(follow, h); // Return the lowest card if you can't win
			} 
			else return ownLowest(follow, h);
		}
		else {
			// If you don't have any cards that follow suit, check trump cards
			suitMatches = h.matches(trump);
			if (!suitMatches.isEmpty()) {
				// If you have any trump cards
				if (highest.suit() == trump) { 
					Card ownHighest = ownHighest(trump, h); // If the highest card is a trump

					if (ownHighest.rank().ordinal() > highest.rank().ordinal()) {
						for (Card c : suitMatches) {
							if (c.rank().ordinal() > highest.rank().ordinal()
							&& c.rank().ordinal() < ownHighest.rank().ordinal()) ownHighest = c;
						}
						return ownHighest; // play the lowest possible card for you to still win
					}
				} 
				else return ownLowest(trump, h); // play your lowest trump card
			}
			return throwAway(follow, trump, h); // You can't win so throw away
		}
	}

	
	/**
	 * Calculates which card to play
	 *
	 * @param highest
	 * @param h
	 * @param trump
	 * @param follow
	 * @return
	 */

	private Card toPlay(Card highest, Hand h, Suit trump, Suit follow) {

		Set<Card> suitMatches = h.matches(follow);

		if (!suitMatches.isEmpty()) {
			if (highest.suit() == follow) { 
				// If the current highest card is also the follow suit
				Card ownH = ownHighest(follow, h);
				if (ownH.rank().ordinal() > highest.rank().ordinal()) return ownH; // Return highest if you can win
				else return ownLowest(follow, h); // Return lowest if you can't win
			} 
			else if (highest.suit() == trump) return ownLowest(follow, h); // If the highest is trump, you cant win
			else return ownLowest(follow, h);
		}

		
		suitMatches = h.matches(trump);

		if (!suitMatches.isEmpty()) {
			if (highest.suit() == trump) { 
				Card ownH = ownHighest(trump, h); // If the highest card is also a trump
				if (ownH.rank().ordinal() > highest.rank().ordinal()) return ownH; // If you can win, return highest
			}
		}
		return throwAway(follow, trump, h); // Throw away card if you can't win

	}

	/**
	 * If you can't win the trick, then throw away the lowest card
	 * 
	 * @param follow Suit to follow
	 * @param trump Trump of the trick
	 * @param h Player's hand
	 * @return
	 */
	private Card throwAway(Suit follow, Suit trump, Hand h) {
		List<Suit> allSuits = withoutTrump(trump);
		allSuits.remove(follow);

		Card low1 = ownLowest(allSuits.get(0), h);
		Card low2 = ownLowest(allSuits.get(1), h);
		if (low1 == null && low2 != null) return low2;
		if (low2 == null && low1 != null) return low1;

		if (low1.rank().ordinal() > low2.rank().ordinal()) return low2;
		else if (low1.rank().ordinal() < low2.rank().ordinal()) return low1;
		else return null;
	}



	/**
	 * Return the highest card that has been played so far
	 *
	 * @param trump
	 * @param follow
	 * @param played
	 * @return
	 */
	private Card highestCard(Suit trump, Suit follow, List<Card> played) {
		Card card = played.get(0);
		for (Card c : played) {
			if (c.suit() == follow) { 
				// if it follows the leading suit
				if (card.rank().ordinal() < c.rank().ordinal()) card = c; // if it is the higher card
			} 
			else if (c.suit() == trump) {
				// if it is the trump suit
				if (card.suit() == trump) { // if the the highest card is also trump
					if (card.rank().ordinal() < c.rank().ordinal()) card = c;
				} 
				else card = c;
			}
		}
		return card;
	}
}