package gpv.chess;

import static gpv.util.Coordinate.makeCoordinate;

import gpv.chess.ChessBehavior.Behavior;

public class ChessRules {

	/**
	 * This function takes in the name of a piece and returns the rules function
	 * 
	 * @param name
	 * @return the rules function if one exists
	 */
	public static Behavior getRules(PieceName name) {

		switch (name) {
		case QUEEN:
			return queenBehavior;
		case KING:
			return kingBehavior;
		case ROOK:
			return rookBehavior;
		case KNIGHT:
			return knightBehavior;
		case PAWN:
			return pawnBehavior;
		case BISHOP:
			return bishopBehavior;
		default:
			return null;
		}
	}

	/*
	 * Beginning of chess piece specific behavior
	 * 
	 */

	/**
	 * This lambda represents the movement behavior of the Queen.
	 * 
	 * @return true if the queen can move to the spot, false if not
	 */
	private static Behavior queenBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) // general behavior
			return false;

		// passed the general behavior test, now it must return true for one of the
		// following behaviors
		return ChessBehavior.straightMovement.allowed(from, to, board)
				|| ChessBehavior.orthogonalMovement.allowed(from, to, board);
	};

	/**
	 * This lambda controls the movement for the rook piece
	 * 
	 * @return true if it is trying to move to a valid spot, otherwise false
	 */
	private static Behavior rookBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) // general behavior
			return false;

		return ChessBehavior.straightMovement.allowed(from, to, board); // rooks only have straight movement
	};

	/**
	 * This function maintains the behavior for the pawn pieces.
	 * 
	 * @author Luke Trujillo
	 */
	private static Behavior pawnBehavior = (from, to, board) -> {
		/*
		 * ASSUMPTION: black pieces can only move downward, (-x) and white pieces can
		 * only move upward (-x)
		 */
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) // general behavior
			return false;

		// check the color specific directions
		ChessPiece piece = (ChessPiece) board.getPieceAt(makeCoordinate((int) from.getX(), (int) from.getY()));
		int distanceX = (int) Math.abs(to.getX() - from.getX()); // get the absolute distance x
		int distanceY = (int) Math.abs(to.getY() - from.getY()); // get the absolute distance y

		int changeX = (int) ((to.getX() - from.getX()) / distanceX); // get the x unit vector
		int changeY = (int) ((to.getY() - from.getY()) / distanceY); // get the x unit vector

		if (piece.getColor() == PlayerColor.BLACK && changeX != -1) { // if the player is black make sure they are
																		// moving the right way
			return false;
		}

		if (piece.getColor() == PlayerColor.WHITE && changeX != 1) { // if they are white, make sure they are moving the
																		// right way
			return false;
		}

		// check if it is within one block
		if (distanceX > 1 || distanceY > 1) {
			return false;
		}

		// now check if it should be a straight move or a diagonal move
		if (changeY == 0) { // straight move
			return ChessBehavior.straightMovement.allowed(from, to, board); // just make sure it is straight movement
		} else { // diagonal move
			if (board.getPieceAt(makeCoordinate((int) from.getX() + changeX, (int) from.getY() + changeY)) == null) {
				return false;
			}

			return ChessBehavior.orthogonalMovement.allowed(from, to, board);
		}
	};

	/**
	 * This lambda controls the movement for the knight piece.
	 * 
	 * @return true if knight can move to the spot, otherwise false.
	 */
	private static Behavior knightBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) // general behavior
			return false;

		final double DISTANCE = Math.sqrt(Math.pow(2, 2) + Math.pow(1, 2)); // a knight can only travel this distance

		int distanceX = (int) Math.abs(to.getX() - from.getX()); // the the delta change in the x
		int distanceY = (int) Math.abs(to.getY() - from.getY()); // get the delta change in the y

		double attempted_distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2)); // get the distance
																								// travelld

		return attempted_distance == DISTANCE; // these two must match
	};

	/**
	 * This function controls the movement for the bishop. Theu can only move
	 * diagonally
	 * 
	 * @return true if the bishop can move to the spot, otherwise false
	 */
	private static Behavior bishopBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) // general behavior
			return false;

		return ChessBehavior.orthogonalMovement.allowed(from, to, board); // the bishop can only move diagonally
	};

	/**
	 * This function controls the movement for the king. They can move any direction
	 * but only to adjacent blocks.
	 * 
	 * @return true if king can move here, otherwise false
	 */
	private static Behavior kingBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) // general behavior
			return false;

		int distanceX = (int) Math.abs(to.getX() - from.getX()); // the the delta change in the x
		int distanceY = (int) Math.abs(to.getY() - from.getY()); // get the delta change in the y

		ChessPiece king = (ChessPiece) board.getPieceAt(makeCoordinate((int) from.getX(), (int) from.getY()));

		if (distanceX == 0 && distanceY == 2 && !king.hasMoved()) { // possbile castling
			int directionY = (int) ((to.getY() - from.getY()) / distanceY); // get the direction of the attempted move

			int rookY = 1; //assume its moving to the left
			if (directionY == 1) { //if it is actually moving to the right
				rookY = 8; //move it to the right
			} 

			ChessPiece rook = (ChessPiece) board.getPieceAt(makeCoordinate((int) from.getX(), rookY)); //get the intented rook

			if (rook != null && rook.getName() == PieceName.ROOK && !rook.hasMoved()
					&& king.getColor() == rook.getColor()) { //if there is a rook that hasnt moved and its on the same team as the king
				return ChessBehavior.straightMovement.allowed(from, to, board); //check for valid straight movement
			}

			return false; //return false at this point
		}

		return (distanceX <= 1 && distanceY <= 1) && (ChessBehavior.orthogonalMovement.allowed(from, to, board)
				|| ChessBehavior.straightMovement.allowed(from, to, board)); // limit the distance to a max change of 1
	};

}
