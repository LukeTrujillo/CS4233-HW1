/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import gpv.PieceDescriptor;
import static gpv.chess.PlayerColor.*;
import static gpv.chess.PieceName.*;
import static gpv.chess.ChessBehavior.*;

/**
 * An enumeration that describes all of the chess piece types with methods
 * to get the color and name.
 * 
 * @version Feb 21, 2020
 */
public enum ChessPieceDescriptor implements PieceDescriptor
{
	WHITEPAWN(WHITE, PAWN, pawnBehavior), 
	WHITEROOK(WHITE, ROOK, rookBehavior),
	WHITEKNIGHT(WHITE, KNIGHT, knightBehavior), 
	WHITEBISHOP(WHITE, BISHOP, bishopBehavior), 
	WHITEQUEEN(WHITE, QUEEN, queenBehavior), 
	WHITEKING(WHITE, KING, kingBehavior),
	BLACKPAWN(BLACK, PAWN, pawnBehavior), 
	BLACKROOK(BLACK, ROOK, rookBehavior),
	BLACKKNIGHT(BLACK, KNIGHT, knightBehavior), 
	BLACKBISHOP(BLACK, BISHOP, bishopBehavior), 
	BLACKQUEEN(BLACK, QUEEN, queenBehavior), 
	BLACKKING(BLACK, KING, kingBehavior);
	
	private PlayerColor color;
	private PieceName name;
	private Behavior behavior;
	
	/**
	 * Private constructor to set the color and name in the instance.
	 * @param color
	 * @param name
	 */
	private ChessPieceDescriptor(PlayerColor color, PieceName name, Behavior behavior)
	{
		this.color = color;
		this.name = name;
		this.behavior = behavior;
	}

	/**
	 * @return the color
	 */
	public PlayerColor getColor()
	{
		return color;
	}

	/**
	 * @return the name
	 */
	public PieceName getName()
	{
		return name;
	}
	
	/**
	 * This will return the behavior assigned to the piece
	 * @return the lambda to be run of the piece
	 */
	public Behavior getMoveBehavior() {
		return behavior;
	}
}
