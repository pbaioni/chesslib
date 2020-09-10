package pbaioni.chesslib;

import pbaioni.chesslib.move.Influence;
import pbaioni.chesslib.move.InfluenceGenerator;
import pbaioni.chesslib.move.Move;

public class main {

	public static void main(String[] args) {
		System.out.println("welcome");
		// TODO Auto-generated method stub
        Board board = new Board();


        board.doMove(new Move(Square.G1, Square.F3));
        Influence influence = InfluenceGenerator.generateInfluence(board);
        board.doMove(new Move(Square.E7, Square.E5));
        influence = InfluenceGenerator.generateInfluence(board);
        System.out.println(influence.toString());
        board.toString();

	}

}
