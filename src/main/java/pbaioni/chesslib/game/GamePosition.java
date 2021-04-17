package pbaioni.chesslib.game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pbaioni.chesslib.move.Move;

public class GamePosition {

	private String fen;

	private Move move;

	private String comment;

	private Integer ply;

	private String arrows;

	private String circles;

	public GamePosition() {

	}

	public GamePosition(String fen, Move move) {
		super();
		this.fen = fen;
		this.move = move;
	}

	public GamePosition(String fen, Move move, Integer ply) {
		super();
		this.fen = fen;
		this.move = move;
		this.ply = ply;
	}

	private String extractGraphics(String comment) {

		StringBuilder temp = new StringBuilder(comment);
		String commentClone = temp.toString();

		Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(commentClone);
		while (m.find()) {
			String content = m.group(1);
			if (content.contains("%cal")) {
				this.arrows = content.substring(content.indexOf("%cal") + 4, content.length()).trim();
				//System.out.println("arrows: " + arrows);
				commentClone = commentClone.replace("[" + content + "]", "");
			}

			if (content.contains("%csl")) {
				this.circles = content.substring(content.indexOf("%csl") + 4, content.length()).trim();
				//System.out.println("circles: " + circles);
				commentClone = commentClone.replace("[" + content + "]", "");
			}

			//System.out.println("clone: " + commentClone);
		}

		return commentClone.trim();
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public String getUciMove() {
		return (this.move.getFrom().name() + this.move.getTo().name()).toLowerCase();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		String commentWithoutGraphics = extractGraphics(comment);
		if (!commentWithoutGraphics.equals("")) {
			this.comment = commentWithoutGraphics;
		}
	}

	public Integer getPly() {
		return ply;
	}

	public void setPly(Integer ply) {
		this.ply = ply;
	}

	public String getArrows() {
		return arrows;
	}

	public void setArrows(String arrows) {
		this.arrows = arrows;
	}

	public String getCircles() {
		return circles;
	}

	public void setCircles(String circles) {
		this.circles = circles;
	}

	@Override
	public String toString() {
		return "GamePosition [fen=" + fen + ", move=" + move + ", comment=" + comment + ", ply=" + ply + ", arrows="
				+ arrows + ", circles=" + circles + "]\n";
	}
}
