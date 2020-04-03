/**
 * This file was developed for CS4233: Object-Oriented Analysis & Design.
 */
package gpv.chess;

import gpv.util.*;

/**
 *  ChessBehavior.java
 *  
 *  [description]
 * 
 *	@author Luke Trujillo
 *  @version Apr 3, 2020
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
		
		if(to.getColumn() <= columns && to.getColumn() > 0) {
			if(to.getRow() <= rows && to.getRow() > 0) {
				return true;
			}
		}
		
		return false;
	};
	
	/**
	 * This lambda will check if the given coordinates to move to have either an enemy piece
	 * or not occupied.
	 * 
	 * @return true if valid, false if not valid
	 * @author Luke Trujillo
	 */
	public static Behavior availableSpace = (from, to, board) -> {
		ChessPiece piece = (ChessPiece) board.getPieceAt(from);
		
		
		
		return true;
	};
}
