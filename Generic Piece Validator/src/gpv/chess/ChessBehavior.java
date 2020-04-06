/**
 * This file was developed for CS4233: Object-Oriented Analysis & Design.
 */
package gpv.chess;

import gpv.util.*;

/**
 * ChessBehavior.java
 * 
 * [description]
 * 
 * @author Luke Trujillo
 * @version Apr 3, 2020
 */
public interface ChessBehavior {

	@FunctionalInterface
	interface Behavior {
		public boolean allowed(Coordinate from, Coordinate to, Board board);
	}

	/**
	 * This lambda will check if the given coordinates to move to are valid
	 * coordinates on the given board.
	 * 
	 * @return true if valid, false if not valid
	 * @author Luke Trujillo
	 */
	public static Behavior withinBounds = (from, to, board) -> {
		int columns = board.getnColumns();
		int rows = board.getnRows();

		if (to.getColumn() <= columns && to.getColumn() > 0) {
			if (to.getRow() <= rows && to.getRow() > 0) {
				return true;
			}
		}

		return false;
	};

	/**
	 * This lambda will check if the given coordinates to move to have either an
	 * enemy piece or not occupied.
	 * 
	 * @return true if valid, false if not valid
	 * @author Luke Trujillo
	 */
	public static Behavior availableSpace = (from, to, board) -> {
		ChessPiece piece = (ChessPiece) board.getPieceAt(from); // get the piece trying to move
		ChessPiece target = (ChessPiece) board.getPieceAt(to); // get the piece at where it is trying to go

		if (target == null) { // empty spot
			return true; // the piece can move there
		}

		if (target.getColor() != piece.getColor()) { // if they are not on the same team
			return true;
		}

		// otherwise it is not an open space so return false
		return false;
	};

	/**
	 * This function wraps the behavior that applies to all pieces inside of one
	 * lambda for simplicity in programming
	 * 
	 * @return true if it passed, false if not
	 */
	public static Behavior generalBehavior = (from, to, board) -> {
		return ChessBehavior.withinBounds.allowed(from, to, board)
				&& ChessBehavior.availableSpace.allowed(from, to, board);
	};

	/**
	 * This function checks if a movement is a vlaid orthogonal movement
	 * 
	 * @return true if valid, false if not
	 */
	public static Behavior orthogonalMovement = (from, to, board) -> {

		int distanceX = (int) Math.abs(to.getX() - from.getX()); // the the delta change in the x
		int distanceY = (int) Math.abs(to.getY() - from.getY()); // get the delta change in the y

		return distanceX == distanceY; // for them to be a valid diagonal this had be true
	};

	/**
	 * This function checks if a movement is a valid straight (x or y) movement
	 * 
	 * @returns true if valid, false if not
	 */
	public static Behavior straightMovement = (from, to, board) -> {

		int distanceX = (int) Math.abs(to.getX() - from.getX()); // the the delta change in the x
		int distanceY = (int) Math.abs(to.getY() - from.getY()); // get the delta change in the y

		return (distanceX != 0 && distanceY == 0) || (distanceY != 0 && distanceX == 0); // this must be true for it to
																							// be a valid straight
																							// movement
	};

	/*
	 * Beginning of chess piece specific behavior
	 * 
	 */

	/**
	 * This lambda represents the movement behavior of the Queen.
	 * @return true if the queen can move to the spot, false if not
	 */
	public static Behavior queenBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) //general behavior
			return false;

		// passed the general behavior test, now it must return true for one of the following behaviors
		return ChessBehavior.straightMovement.allowed(from, to, board)
				|| ChessBehavior.orthogonalMovement.allowed(from, to, board);
	};
	
	/**
	 * This lambda controls the movement for the rook piece
	 * @return true if it is trying to move to a valid spot, otherwise false
	 */
	public static Behavior rookBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) //general behavior
			return false;
		
		return ChessBehavior.straightMovement.allowed(from, to, board); //rooks only have straight movement
	};
	
	public static Behavior pawnBehavior = (from, to, board) -> {
		return false;
	};
	
	/**
	 * This lambda controls the movement for the knight piece. 
	 * @return true if knight can move to the spot, otherwise false.
	 */
	public static Behavior knightBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) //general behavior
			return false;
		
		final double DISTANCE = Math.sqrt(Math.pow(2, 2) + Math.pow(1, 2)); // a knight can only travel this distance
		
		int distanceX = (int) Math.abs(to.getX() - from.getX()); // the the delta change in the x
		int distanceY = (int) Math.abs(to.getY() - from.getY()); // get the delta change in the y
		
		double attempted_distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2)); //get the distance travelld

		return attempted_distance == DISTANCE; // these two must match
	};
	
	/**
	 * This function controls the movment for the bishop. Theu can only move diagonally
	 * @return true if the bishop can move to the spot, otherwise false
	 */
	public static Behavior bishopBehavior = (from, to, board) -> {
		if (!ChessBehavior.generalBehavior.allowed(from, to, board)) //general behavior
			return false;	
		
		return ChessBehavior.orthogonalMovement.allowed(from, to, board); //the bishop can only move diagonally
	};
	public static Behavior kingBehavior = (from, to, board) -> {
		return false;
	};
	

}
