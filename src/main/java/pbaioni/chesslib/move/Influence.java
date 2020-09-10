package pbaioni.chesslib.move;

import java.util.HashMap;

import pbaioni.chesslib.Side;
import pbaioni.chesslib.Square;

public class Influence {
	
	private HashMap<Square, Integer> influence = new HashMap<Square, Integer>();
	
	public Influence() {
		
	}
	
	public HashMap<Square, Integer> getInfluence() {
		return influence;
	}

	public void setInfluence(HashMap<Square, Integer> influence) {
		this.influence = influence;
	}

	@Override
	public String toString() {
		return "Influence [influence=" + influence + "]";
	}

	
	public void addSingleInfluence(Square square, Side side) {
		
		int delta = 1;
		if(side.equals(Side.BLACK)) {
			delta = -1;
		}
		
		if(influence.containsKey(square)) {
			influence.replace(square, influence.get(square) + delta);
		}else {
			influence.put(square, delta);
		}
	}
	
	public Influence addInfluence(Influence influenceToAdd) {
		for(Square square : influenceToAdd.getInfluence().keySet()) {
			if(influence.containsKey(square)) {
				influence.replace(square, influence.get(square) + influenceToAdd.getInfluence().get(square));
			}else {
				influence.put(square, influenceToAdd.getInfluence().get(square));
			}
		}
		
		return this;
		
	}
	
	public Influence mergeInfluence(Influence InfluenceToMerge) {
		for(Square square : InfluenceToMerge.getInfluence().keySet()) {
			if(!this.influence.containsKey(square)) {
				this.influence.put(square, InfluenceToMerge.getInfluence().get(square));
			}
		}
		return this;
	}


	public void clear() {
		for(Square square : this.influence.keySet()) {
			this.influence.remove(square);
		}
		
	}

	
	
}
