// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package swen221.cards.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a trick being played. This includes the cards that have been
 * played so far, as well as what the suit of trumps is for this trick.
 *
 * @author David J. Pearce
 *
 */
public class Trick implements Cloneable {
	private Card[] cards = new Card[4];
	private Player.Direction lead;
	private Card.Suit trumps;
	private Card.Suit leadSuit;

	/**
	 * Contruct a new trick with a given lead player and suit of trumps.
	 *
	 * @param lead
	 *            --- lead player for this trick.
	 * @param trumps
	 *            --- maybe null if no trumps.
	 */
	public Trick(Player.Direction lead, Card.Suit trumps) {
		this.lead = lead;
		this.trumps = trumps;
	}

	/**
	 * Determine who the lead player for this trick is.
	 *
	 * @return The direction of the lead player.
	 */
	public Player.Direction getLeadPlayer() {
		return lead;
	}

	/**
	 * Determine which suit are trumps for this trick, or <code>null</code> if there
	 * are no trumps.
	 *
	 * @return The current suit of trumps
	 */
	public Card.Suit getTrumps() {
		return trumps;
	}
	
	public Card[] getCards() {
		return cards;
	}

	/**
	 * Get the list of cards played so far in the order they were played.
	 *
	 * @return The list of cards played so far.
	 */
	public List<Card> getCardsPlayed() {
		ArrayList<Card> cs = new ArrayList<>();
		for(int i = 0;i < 4; i++) {
			if(cards[i] != null) {
				cs.add(cards[i]);
			}
		}
		return cs;
	}

	/**
	 * Get the card played by a given player, or null if that player has yet to
	 * play.
	 *
	 * @param p --- player
	 * @return The card played by the player.
	 */
	public Card getCardPlayed(Player.Direction p) {
		Player.Direction player = lead;
		for(int i=0;i!=4;++i) {
			if(player.equals(p)) {
				return cards[i];
			}
			player = player.next();
		}
		// deadcode
		return null;
	}

	/**
	 * Determine the next player to play in this trick.
	 *
	 * @return The next player to play.
	 */
	public Player.Direction getNextToPlay() {
		Player.Direction dir = lead;
		for(int i=0;i!=4;++i) {
			if(cards[i] == null) {
				return dir;
			}
			dir = dir.next();
		}
		return null;
	}

	/**
	 * Determine the winning player for this trick. This requires looking to see
	 * which player led the highest card that followed suit; or, was a trump.
	 *
	 * @return The winning player (thus far).
	 */
	public Player.Direction getWinner() {
		Player.Direction player = lead;
		Player.Direction winningPlayer = null;
		Card winningCard = cards[0];
		for (int i = 0; i != 4; ++i) {
			if (cards[i].suit() == winningCard.suit()
					&& cards[i].compareTo(winningCard) >= 0) {
				winningPlayer = player;
				winningCard = cards[i];
			} else if (trumps != null && cards[i].suit() == trumps
					&& winningCard.suit() != trumps) {
				// in this case, the winning card is a trump
				winningPlayer = player;
				winningCard = cards[i];
			}
			player = player.next();
		}
		return winningPlayer;
	}
	
	/**
	 * Clones the trick
	 */
	@Override
	public Trick clone(){
		Trick cloned = new Trick(this.lead, this.trumps);
		List<Card> cardsToClone = this.getCardsPlayed();
		for(int i = 0; i < cardsToClone.size(); i++) {
			cloned.cards[i] = cardsToClone.get(i).clone();
		}
		cloned.lead = this.lead;
		cloned.trumps = this.trumps;
		return cloned;
	}

	/**
	 * Player attempts to play a card. This method checks that the given player is
	 * entitled to play, and that the played card follows suit. If either of these
	 * are not true, it throws an IllegalMove exception.
	 *
	 * @param p The player who is playing the card.
	 * @param c The card being played.
	 * @throws IllegalMove If the player / card combination is invalid.
	 */
	public void play(Player p, Card c) throws IllegalMove {
		if(!inSequence(p,c)) throw new IllegalMove("Illegal move: player is out of sequence.");
		if(!inHand(p,c)) throw new IllegalMove("Illegal move: card is not in hand.");
		if(!followsSuit(p,c)) throw new IllegalMove("Illegal move: card does not follow suit.");
		

		// Finally, play the card.
		for (int i = 0; i != 4; ++i) {
			if (cards[i] == null) {
				if(p.getDirection().equals(lead)) leadSuit = c.suit();
				cards[i] = c;
				p.getHand().remove(c);
				break;
			}
		}
	}
	
	/**
	 * Checks that the player is the next to play
	 * If out of sequence, returns false
	 * @param p The player playing the card
	 * @param c The card to be played
	 * @return
	 */
	public boolean inSequence(Player p, Card c) {
		if(p.getDirection().equals(getNextToPlay())) return true;
		else return false;
	}
	
	/**
	 * Checks that the card is a valid card in the player's hand
	 * If it is invalid, returns false
	 * @param p The player playing the card
	 * @param c The card to be played
	 * @return
	 */
	public boolean inHand(Player p, Card c){
		if(p.getHand().contains(c)) return true;
		return false;
	}
	
	/**
	 * Checks that the card follows suit of the trick
	 * @param p The player playing the card
	 * @param c The card to be played
	 * @return
	 */
	public boolean followsSuit(Player p, Card c) {
		if(p.getDirection().equals(lead)) return true;
		for(Card card : p.getHand()) {
			if(card.suit().equals(leadSuit) && !c.suit().equals(leadSuit)) return false;
		}
		return true;
	}
	
	
}
