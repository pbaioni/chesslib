package pbaioni.chesslib.game;

public class GamePosition {
	
	private String fen;
	
	private String move;
	
	private String comment;
	
	private Integer ply;
	
	public GamePosition() {
		
	}

	public GamePosition(String fen, String move) {
		super();
		this.fen = fen;
		this.move = move;
	}

	public GamePosition(String fen, String move, Integer ply) {
		super();
		this.fen = fen;
		this.move = move;
		this.ply = ply;
	}

	public GamePosition(String fen, String move, String comment) {
		super();
		this.fen = fen;
		this.move = move;
		this.comment = comment;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getPly() {
		return ply;
	}

	public void setPly(Integer ply) {
		this.ply = ply;
	}

	@Override
	public String toString() {
		return "GamePosition [fen=" + fen + ", move=" + move + ", comment=" + comment + ", ply=" + ply + "]\n";
	}



}
