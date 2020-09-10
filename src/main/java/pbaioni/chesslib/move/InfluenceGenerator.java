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

package pbaioni.chesslib.move;

import static pbaioni.chesslib.Bitboard.bitScanForward;
import static pbaioni.chesslib.Bitboard.extractLsb;

import pbaioni.chesslib.*;

/**
 * The Move generator.
 */
public class InfluenceGenerator {

    private InfluenceGenerator() {
    }
    

    public static Influence generateInfluence(Board board) {
        Influence boardInfluence = new Influence();
        Side side = board.getSideToMove();
        boardInfluence.addInfluence(getAllPawnInfluence(board, side));
        boardInfluence.addInfluence(getAllKnightInfluence(board, side));
        boardInfluence.addInfluence(getAllBishopInfluence(board, side));
        boardInfluence.addInfluence(getAllRookInfluence(board, side));
        boardInfluence.addInfluence(getAllQueenInfluence(board, side));
        boardInfluence.addInfluence( getAllKingInfluence(board, side));

        return boardInfluence;
    }
    
    public static Influence getAllPawnInfluence(Board board, Side side) {

    	return getPawnInfluence(board, side).mergeInfluence(getPawnInfluence(board, side.flip()));

    }
    
    public static Influence getPawnInfluence(Board board, Side side) {
    	Influence pawnInfluence = new Influence();
        long pieces = board.getBitboard(Piece.make(side, PieceType.PAWN));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);  
            
            long attacks = Bitboard.getAbsolutePawnCaptures(side, sqSource,board.getBitboard(), board.getEnPassantTarget());
            while (attacks != 0L) {
                int targetIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(targetIndex);
                pawnInfluence.addSingleInfluence(sqTarget, side);
            }
        }
        
        return pawnInfluence;
    }

    public static Influence getAllKnightInfluence(Board board, Side side) {
    	

    	Influence side1Influence = getKnightInfluence(board, side, ~board.getBitboard(side))
    			.mergeInfluence(getKnightInfluence(board, side, ~board.getBitboard(side.flip())));

    	Influence side2Influence = getKnightInfluence(board, side.flip(), ~board.getBitboard(side.flip()))
    			.mergeInfluence(getKnightInfluence(board, side.flip(), ~board.getBitboard(side)));
        
    	return side1Influence.addInfluence(side2Influence);
    }

    public static Influence getKnightInfluence(Board board, Side side, long mask) {

    	Influence knightInfluence = new Influence();
        long pieces = board.getBitboard(Piece.make(side, PieceType.KNIGHT));
        while (pieces != 0L) {
            int knightIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(knightIndex);
            long attacks = Bitboard.getKnightAttacks(sqSource, mask);
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                knightInfluence.addSingleInfluence(sqTarget, side);
            }
        }
        
        return knightInfluence;
    }

    
    public static Influence getAllBishopInfluence(Board board, Side side) {
    	
    	Influence side1Influence = getBishopInfluence(board, side, ~board.getBitboard(side))
    			.mergeInfluence(getBishopInfluence(board, side, ~board.getBitboard(side.flip())));

    	Influence side2Influence = getBishopInfluence(board, side.flip(), ~board.getBitboard(side.flip()))
    			.mergeInfluence(getBishopInfluence(board, side.flip(), ~board.getBitboard(side)));
        
    	return side1Influence.addInfluence(side2Influence);

    }

    public static Influence getBishopInfluence(Board board, Side side, long mask) {

    	Influence bishopInfluence = new Influence();
        long pieces = board.getBitboard(Piece.make(side, PieceType.BISHOP));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getBishopAttacks(board.getBitboard(), sqSource) & mask;
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                bishopInfluence.addSingleInfluence(sqTarget, side);
            }
        }
        return bishopInfluence;
    }

    public static Influence getAllRookInfluence(Board board, Side side) {
    	
    	Influence side1Influence = getRookInfluence(board, side, ~board.getBitboard(side))
    			.mergeInfluence(getRookInfluence(board, side, ~board.getBitboard(side.flip())));

    	Influence side2Influence = getRookInfluence(board, side.flip(), ~board.getBitboard(side.flip()))
    			.mergeInfluence(getRookInfluence(board, side.flip(), ~board.getBitboard(side)));
        
    	return side1Influence.addInfluence(side2Influence);

    }

    public static Influence getRookInfluence(Board board, Side side, long mask) {

    	Influence rookInfluence = new Influence();
        long pieces = board.getBitboard(Piece.make(side, PieceType.ROOK));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getRookAttacks(board.getBitboard(), sqSource) & mask;
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                rookInfluence.addSingleInfluence(sqTarget, side);
            }
        }
        
        return rookInfluence;
    }

    public static Influence getAllQueenInfluence(Board board, Side side) {
    	
    	Influence side1Influence = getQueenInfluence(board, side, ~board.getBitboard(side))
    			.mergeInfluence(getQueenInfluence(board, side, ~board.getBitboard(side.flip())));

    	Influence side2Influence = getQueenInfluence(board, side.flip(), ~board.getBitboard(side.flip()))
    			.mergeInfluence(getQueenInfluence(board, side.flip(), ~board.getBitboard(side)));
        
    	return side1Influence.addInfluence(side2Influence);

    }

    public static Influence getQueenInfluence(Board board, Side side, long mask) {

    	Influence queenInfluence = new Influence();
        long pieces = board.getBitboard(Piece.make(side, PieceType.QUEEN));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getQueenAttacks(board.getBitboard(), sqSource) & mask;
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                queenInfluence.addSingleInfluence(sqTarget, side);
            }
        }
        
        return queenInfluence;
    }


    public static Influence getAllKingInfluence(Board board, Side side) {
    	
    	Influence side1Influence = getKingInfluence(board, side, ~board.getBitboard(side))
    			.mergeInfluence(getKingInfluence(board, side, ~board.getBitboard(side.flip())));

    	Influence side2Influence = getKingInfluence(board, side.flip(), ~board.getBitboard(side.flip()))
    			.mergeInfluence(getKingInfluence(board, side.flip(), ~board.getBitboard(side)));
        
    	return side1Influence.addInfluence(side2Influence);

    }


    public static Influence getKingInfluence(Board board, Side side, long mask) {

    	Influence kingInfluence = new Influence();
        long pieces = board.getBitboard(Piece.make(side, PieceType.KING));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getKingAttacks(sqSource, mask);
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                kingInfluence.addSingleInfluence(sqTarget, side);
            }
        }
        
        return kingInfluence;
    }

}
