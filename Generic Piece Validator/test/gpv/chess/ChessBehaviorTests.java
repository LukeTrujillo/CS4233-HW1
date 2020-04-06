package gpv.chess;

import org.junit.jupiter.api.*;

import gpv.util.Board;
import gpv.util.Coordinate;

import static org.junit.Assert.*;
import static gpv.chess.ChessPieceDescriptor.*;
import static gpv.util.Coordinate.makeCoordinate;

public class ChessBehaviorTests {

	private static ChessPieceFactory factory = null;
	private Board board;
	
	@BeforeAll
	public static void setupBeforeTests()
	{
		factory = new ChessPieceFactory();
	}
	
	@BeforeEach
	public void setupTest()
	{
		board = new Board(8, 8);
	}
	
	@Test
	public void testWithinValidBounds() {
		for(int x = 1; x <= 8; x++) {
			for (int y = 1; y <= 8; y++) {
				assertTrue(ChessBehavior.withinBounds.allowed(makeCoordinate(x, y), makeCoordinate(x, y), board));
			}
		}
	}
	
	@Test
	public void testNegativesGivenInWithinBounds() {
		assertFalse(ChessBehavior.withinBounds.allowed(makeCoordinate(1, 1), makeCoordinate(-5, 5), board));
		assertFalse(ChessBehavior.withinBounds.allowed(makeCoordinate(1, 1), makeCoordinate(5, -5), board));
	}
	
	@Test
	public void testZeroCoordinateGivenInWithinBounds() {
		assertFalse(ChessBehavior.withinBounds.allowed(makeCoordinate(1, 1), makeCoordinate(5, 0), board));
		assertFalse(ChessBehavior.withinBounds.allowed(makeCoordinate(1, 1), makeCoordinate(0, 5), board));
	}
	
	@Test
	public void testOverboundsGivenInWithinBounds() { 
		assertFalse(ChessBehavior.withinBounds.allowed(makeCoordinate(1, 1), makeCoordinate(9, 1), board));
		assertFalse(ChessBehavior.withinBounds.allowed(makeCoordinate(1, 1), makeCoordinate(1, 9), board));
	}
	
	//ChessBehavior.availableSpace.allowed tests
	
	@Test
	public void testEmptySpotIsAvailableSpace() {
		for(int x = 1; x < 8; x++) {
			for (int y = 1; y < 8; y++) {
				assertTrue(ChessBehavior.availableSpace.allowed(makeCoordinate(x, y), makeCoordinate(x + 1, y + 1), board));
			}
		}
	}
	
	@Test
	public void testEnemyOccupiedSpotIsAvailableSpace() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));
		
		assertTrue(ChessBehavior.availableSpace.allowed(makeCoordinate(3, 5), makeCoordinate(2, 6), board));
	}
	
	@Test
	public void testAlliedOccupiedSpotIsAvailableSpace() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(BLACKROOK);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));
		
		assertFalse(ChessBehavior.availableSpace.allowed(makeCoordinate(3, 5), makeCoordinate(2, 6), board));
	}
	
	
	// ChessBehavior.orthongonalMovement.allowed tests
	
	@Test
	public void testValidOrthongonalMoves() {
		assertTrue(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(1, 1), board));
		assertTrue(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(8, 8), board));
		assertTrue(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(3, 1), board));
		assertTrue(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(1, 3), board));
	}
	
	@Test
	public void testInvalidOrthongonalMoves() {
		assertFalse(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(1, 2), board));
		assertFalse(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(3, 3), makeCoordinate(3, 5), board));
	}
	
	//ChessBehavior.straightMovement.allowed tests
	@Test
	public void testValidXStraightMovment() {
		assertTrue(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 1), makeCoordinate(8, 1), board));
		assertTrue(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 8), makeCoordinate(8, 8), board));
		
	}
	@Test
	public void testValidYStraightMovment() {
		assertTrue(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 1), makeCoordinate(1, 8), board));
		assertTrue(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 8), makeCoordinate(1, 1), board));	
	}
	@Test
	public void testInvalidStraightMovment() {
		assertFalse(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 1), makeCoordinate(8, 8), board));
		assertFalse(ChessBehavior.straightMovement.allowed(makeCoordinate(8, 8), makeCoordinate(1, 1), board));	
	}
	
	//Knight Piece Test
	@Test 
	public void testAllValidLKnightMoves() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(5,2), board));
		assertTrue(bn.canMove(c, makeCoordinate(3,2), board));
		assertTrue(bn.canMove(c, makeCoordinate(6,3), board));
		assertTrue(bn.canMove(c, makeCoordinate(6,5), board));
		assertTrue(bn.canMove(c, makeCoordinate(5,6), board));
		assertTrue(bn.canMove(c, makeCoordinate(3,6), board));
		assertTrue(bn.canMove(c, makeCoordinate(2,3), board));
		assertTrue(bn.canMove(c, makeCoordinate(2,5), board));
	}
	
	@Test 
	public void testInvalidKnightMoves() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);
		
		assertFalse(bn.canMove(c, makeCoordinate(4,8), board));
		assertFalse(bn.canMove(c, makeCoordinate(8,4), board));
		assertFalse(bn.canMove(c, makeCoordinate(8,8), board));
		assertFalse(bn.canMove(c, makeCoordinate(1,1), board));
	}
}