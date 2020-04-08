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
	public static void setupBeforeTests() {
		factory = new ChessPieceFactory();
	}

	@BeforeEach
	public void setupTest() {
		board = new Board(8, 8);
	}

	@Test // Test 1
	public void testWithinValidBounds() {
		for (int x = 1; x <= 8; x++) {
			for (int y = 1; y <= 8; y++) {
				assertTrue(ChessBehavior.withinBounds.allowed(makeCoordinate(x, y), makeCoordinate(x, y), board));
			}
		}
	}

	@Test // Test 2
	public void testNegativesGivenInWithinBounds() {
		assertFalse(ChessBehavior.generalBehavior.allowed(makeCoordinate(1, 1), makeCoordinate(-5, 5), board));
		assertFalse(ChessBehavior.generalBehavior.allowed(makeCoordinate(1, 1), makeCoordinate(5, -5), board));
	}

	@Test // Test 3
	public void testZeroCoordinateGivenInWithinBounds() {
		assertFalse(ChessBehavior.generalBehavior.allowed(makeCoordinate(1, 1), makeCoordinate(5, 0), board));
		assertFalse(ChessBehavior.generalBehavior.allowed(makeCoordinate(1, 1), makeCoordinate(0, 5), board));
	}

	@Test // Test 4
	public void testOverboundsGivenInWithinBounds() {
		assertFalse(ChessBehavior.generalBehavior.allowed(makeCoordinate(1, 1), makeCoordinate(9, 1), board));
		assertFalse(ChessBehavior.generalBehavior.allowed(makeCoordinate(1, 1), makeCoordinate(1, 9), board));
	}

	// ChessBehavior.availableSpace.allowed tests

	@Test // Test 5
	public void testEmptySpotIsAvailableSpace() {
		for (int x = 1; x < 8; x++) {
			for (int y = 1; y < 8; y++) {
				assertTrue(ChessBehavior.availableSpace.allowed(makeCoordinate(x, y), makeCoordinate(x + 1, y + 1),
						board));
			}
		}
	}

	@Test // Test 6
	public void testEnemyOccupiedSpotIsAvailableSpace() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));

		assertTrue(ChessBehavior.availableSpace.allowed(makeCoordinate(3, 5), makeCoordinate(2, 6), board));
	}

	@Test // Test 7
	public void testAlliedOccupiedSpotIsAvailableSpace() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(BLACKROOK);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));

		assertFalse(ChessBehavior.availableSpace.allowed(makeCoordinate(3, 5), makeCoordinate(2, 6), board));
	}

	// ChessBehavior.orthongonalMovement.allowed tests

	@Test // Test 8
	public void testValidOrthongonalMoves() {
		assertTrue(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(1, 1), board));
		assertTrue(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(8, 8), board));
		assertTrue(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(3, 1), board));
		assertTrue(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(1, 3), board));
	}

	@Test // Test 9
	public void testInvalidOrthongonalMoves() {
		assertFalse(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(2, 2), makeCoordinate(1, 2), board));
		assertFalse(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(3, 3), makeCoordinate(3, 5), board));
		assertFalse(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(3, 3), makeCoordinate(3, 4), board));
		assertFalse(ChessBehavior.orthogonalMovement.allowed(makeCoordinate(3, 3), makeCoordinate(4, 3), board));
	}

	// ChessBehavior.straightMovement.allowed tests
	@Test // Test 10
	public void testValidXStraightMovment() {
		assertTrue(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 1), makeCoordinate(8, 1), board));
		assertTrue(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 8), makeCoordinate(8, 8), board));

	}

	@Test // Test 11
	public void testValidYStraightMovment() {
		assertTrue(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 1), makeCoordinate(1, 8), board));
		assertTrue(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 8), makeCoordinate(1, 1), board));
	}

	@Test // Test 12
	public void testInvalidStraightMovment() {
		assertFalse(ChessBehavior.straightMovement.allowed(makeCoordinate(1, 1), makeCoordinate(8, 8), board));
		assertFalse(ChessBehavior.straightMovement.allowed(makeCoordinate(8, 8), makeCoordinate(1, 1), board));
	}

	// Knight Piece Test
	@Test // Test 13
	public void testAllValidLKnightMoves() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(5, 2), board));
		assertTrue(bn.canMove(c, makeCoordinate(3, 2), board));
		assertTrue(bn.canMove(c, makeCoordinate(6, 3), board));
		assertTrue(bn.canMove(c, makeCoordinate(6, 5), board));
		assertTrue(bn.canMove(c, makeCoordinate(5, 6), board));
		assertTrue(bn.canMove(c, makeCoordinate(3, 6), board));
		assertTrue(bn.canMove(c, makeCoordinate(2, 3), board));
		assertTrue(bn.canMove(c, makeCoordinate(2, 5), board));
	}

	@Test // Test 14
	public void testInvalidKnightMoves() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertFalse(bn.canMove(c, makeCoordinate(4, 8), board));
		assertFalse(bn.canMove(c, makeCoordinate(8, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(8, 8), board));
		assertFalse(bn.canMove(c, makeCoordinate(1, 1), board));
	}

	// Queen piece movement
	@Test // Test 15
	public void testQueenCanMoveOrthonally() {
		ChessPiece bn = factory.makePiece(BLACKQUEEN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(1, 1), board));
		assertTrue(bn.canMove(c, makeCoordinate(7, 1), board));
		assertTrue(bn.canMove(c, makeCoordinate(8, 8), board));
		assertTrue(bn.canMove(c, makeCoordinate(1, 7), board));
	}

	@Test // Test 16
	public void testQueenCanMoveStraight() {
		ChessPiece bn = factory.makePiece(BLACKQUEEN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(4, 8), board));
		assertTrue(bn.canMove(c, makeCoordinate(4, 1), board));
		assertTrue(bn.canMove(c, makeCoordinate(8, 4), board));
		assertTrue(bn.canMove(c, makeCoordinate(1, 4), board));
	}

	@Test // Test 17
	public void testQueenCanNotDoLMove() {
		ChessPiece bn = factory.makePiece(BLACKQUEEN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertFalse(bn.canMove(c, makeCoordinate(5, 2), board));
		assertFalse(bn.canMove(c, makeCoordinate(3, 6), board));
	}

	// Tests for the Rook piece
	@Test // Test 18
	public void testRookCanMoveXDirection() {
		ChessPiece bn = factory.makePiece(BLACKROOK);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(8, 4), board));
		assertTrue(bn.canMove(c, makeCoordinate(1, 4), board));
	}

	@Test // Test 19
	public void testRookCanMoveYDirection() {
		ChessPiece bn = factory.makePiece(BLACKROOK);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(4, 8), board));
		assertTrue(bn.canMove(c, makeCoordinate(4, 1), board));
	}

	// Tests for the bishop
	@Test // Test 20
	public void testBishopCanMoveOrthonally() {
		ChessPiece bn = factory.makePiece(BLACKBISHOP);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(1, 1), board));
		assertTrue(bn.canMove(c, makeCoordinate(7, 1), board));
		assertTrue(bn.canMove(c, makeCoordinate(8, 8), board));
		assertTrue(bn.canMove(c, makeCoordinate(1, 7), board));
	}

	@Test // Test 21
	public void testBishopCanNotMoveStraight() {
		ChessPiece bn = factory.makePiece(BLACKBISHOP);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertFalse(bn.canMove(c, makeCoordinate(8, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(1, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(4, 8), board));
		assertFalse(bn.canMove(c, makeCoordinate(4, 1), board));
	}

	// King piece test
	@Test // Test 22
	public void testKingValidMoves() {
		ChessPiece bn = factory.makePiece(BLACKKING);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(4, 5), board));
		assertTrue(bn.canMove(c, makeCoordinate(5, 4), board));
		assertTrue(bn.canMove(c, makeCoordinate(5, 3), board));
		assertTrue(bn.canMove(c, makeCoordinate(5, 5), board));

		assertTrue(bn.canMove(c, makeCoordinate(3, 5), board));
		assertTrue(bn.canMove(c, makeCoordinate(3, 4), board));
		assertTrue(bn.canMove(c, makeCoordinate(3, 3), board));
		assertTrue(bn.canMove(c, makeCoordinate(4, 3), board));
	}

	@Test // Test 23
	public void testKingInvalidMoves() {
		ChessPiece bn = factory.makePiece(BLACKKING);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertFalse(bn.canMove(c, makeCoordinate(6, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(4, 6), board));
		assertFalse(bn.canMove(c, makeCoordinate(6, 6), board));
		assertFalse(bn.canMove(c, makeCoordinate(2, 2), board));

		assertFalse(bn.canMove(c, makeCoordinate(4, 2), board));
		assertFalse(bn.canMove(c, makeCoordinate(2, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(2, 6), board));
		assertFalse(bn.canMove(c, makeCoordinate(6, 2), board));
	}

	// Test pawn behavior
	@Test // Test 24
	public void testBlackBackwardMovement() {
		ChessPiece bn = factory.makePiece(BLACKPAWN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(3, 4), board));
	}

	@Test // Test 25
	public void testBlackPawnOnlyBackwardMovement() {
		ChessPiece bn = factory.makePiece(BLACKPAWN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertFalse(bn.canMove(c, makeCoordinate(4, 5), board));
		assertFalse(bn.canMove(c, makeCoordinate(5, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(3, 3), board));
		assertFalse(bn.canMove(c, makeCoordinate(3, 5), board));
	}

	@Test // Test 26
	public void testBlackPawnBackwardDiagonalWithEnemy() {
		ChessPiece bn = factory.makePiece(BLACKPAWN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(3, 3));
		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(3, 5));

		assertTrue(bn.canMove(c, makeCoordinate(3, 3), board));
		assertTrue(bn.canMove(c, makeCoordinate(3, 5), board));
	}

	@Test // Test 27
	public void testBlackPawnNoBackwardsDiagonalWithEnemy() {
		ChessPiece bn = factory.makePiece(BLACKPAWN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(5, 3));
		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(5, 5));

		assertFalse(bn.canMove(c, makeCoordinate(5, 3), board));
		assertFalse(bn.canMove(c, makeCoordinate(5, 5), board));
	}

	// white pawns now
	@Test // Test 28
	public void testWhitePawnForwardMovement() {
		ChessPiece bn = factory.makePiece(WHITEPAWN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertTrue(bn.canMove(c, makeCoordinate(5, 4), board));

	}

	@Test // Test 29
	public void testWhitePawnOnlyForwardMovement() {
		ChessPiece bn = factory.makePiece(WHITEPAWN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertFalse(bn.canMove(c, makeCoordinate(4, 3), board));
		assertFalse(bn.canMove(c, makeCoordinate(3, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(5, 3), board));
		assertFalse(bn.canMove(c, makeCoordinate(5, 5), board));
	}

	@Test // Test 30
	public void testWhitePawnForwardDiagonalWithEnemy() {
		ChessPiece bn = factory.makePiece(WHITEPAWN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(5, 3));
		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(5, 5));

		assertTrue(bn.canMove(c, makeCoordinate(5, 3), board));
		assertTrue(bn.canMove(c, makeCoordinate(5, 5), board));
	}

	@Test // Test 31
	public void testWhitePawnNoBackwardsDiagonalWithEnemy() {
		ChessPiece bn = factory.makePiece(WHITEPAWN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(3, 3));
		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(3, 5));

		assertFalse(bn.canMove(c, makeCoordinate(3, 3), board));
		assertFalse(bn.canMove(c, makeCoordinate(3, 5), board));
	}

	// ChessBehavior.clearPath.allowed(from, to, board) tests

	@Test // Test 32
	public void testXYTranversalIsBlockedIfPathObstructed() {
		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(7, 4));
		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(2, 4));
		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(4, 2));
		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(4, 7));

		ChessPiece bn = factory.makePiece(WHITEQUEEN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertFalse(bn.canMove(c, makeCoordinate(8, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(1, 4), board));
		assertFalse(bn.canMove(c, makeCoordinate(4, 8), board));
		assertFalse(bn.canMove(c, makeCoordinate(4, 1), board));
	}

	@Test // Test 33
	public void testDiagonalTranversalIsBlockedIfPathObstructed() {
		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(6, 2));
		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(7, 7));
		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(2, 6));
		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(2, 2));

		ChessPiece bn = factory.makePiece(WHITEQUEEN);
		Coordinate c = makeCoordinate(4, 4);
		board.putPieceAt(bn, c);

		assertFalse(bn.canMove(c, makeCoordinate(7, 1), board));
		assertFalse(bn.canMove(c, makeCoordinate(1, 7), board));
		assertFalse(bn.canMove(c, makeCoordinate(8, 8), board));
		assertFalse(bn.canMove(c, makeCoordinate(1, 1), board));
	}

	@Test // Test 34
	public void testCastlingCase() {
		ChessPiece rook1 = factory.makePiece(WHITEROOK);
		ChessPiece king = factory.makePiece(WHITEKING);
		
		ChessPiece rook2 = factory.makePiece(WHITEROOK);

		board.putPieceAt(rook1, makeCoordinate(1, 1));
		board.putPieceAt(rook2, makeCoordinate(1, 8));
		board.putPieceAt(king, makeCoordinate(1, 5));

		assertFalse(rook1.hasMoved());
		assertFalse(rook2.hasMoved());
		assertFalse(king.hasMoved());
		
		assertTrue(king.canMove(makeCoordinate(1, 5), makeCoordinate(1, 3), board));
		assertTrue(king.canMove(makeCoordinate(1, 5), makeCoordinate(1, 7), board));
	}
	
	@Test  //Test 35
	public void testHasMovedCantCastle() {
		ChessPiece rook1 = factory.makePiece(WHITEROOK);
		ChessPiece king = factory.makePiece(WHITEKING);
		ChessPiece rook2 = factory.makePiece(WHITEROOK);

		board.putPieceAt(rook1, makeCoordinate(1, 1));
		board.putPieceAt(rook2, makeCoordinate(1, 8));
		board.putPieceAt(king, makeCoordinate(1, 5));
		
		assertFalse(rook1.hasMoved());
		assertFalse(rook2.hasMoved());
		assertFalse(king.hasMoved());

		rook1.setHasMoved();
		assertFalse(king.canMove(makeCoordinate(1, 5), makeCoordinate(1, 3), board));
		
		rook2.setHasMoved();
		assertFalse(king.canMove(makeCoordinate(1, 5), makeCoordinate(1, 7), board));
		
		rook1 = factory.makePiece(WHITEROOK);
		rook2 = factory.makePiece(WHITEROOK);

		board.putPieceAt(rook1, makeCoordinate(1, 1));
		board.putPieceAt(rook2, makeCoordinate(1, 8));
		
		assertFalse(rook1.hasMoved());
		assertFalse(rook2.hasMoved());
		assertFalse(king.hasMoved());
		
		king.setHasMoved();
		assertFalse(king.canMove(makeCoordinate(1, 5), makeCoordinate(1, 3), board));
		assertFalse(king.canMove(makeCoordinate(1, 5), makeCoordinate(1, 7), board));
	}
	
	@Test //Test 36
	public void testNoRookCastlingFails() {
		ChessPiece king = factory.makePiece(BLACKKING);
		board.putPieceAt(king, makeCoordinate(8, 5));
		
		assertFalse(king.hasMoved());

		assertFalse(king.canMove(makeCoordinate(8, 5), makeCoordinate(8, 3), board));
		assertFalse(king.canMove(makeCoordinate(8, 5), makeCoordinate(8, 7), board));
	}
	
	@Test //Test #37
	public void testCastlingFailsWhenItemsInTheWay() {
		ChessPiece rook1 = factory.makePiece(WHITEROOK);
		ChessPiece king = factory.makePiece(BLACKKING);
		ChessPiece rook2 = factory.makePiece(WHITEROOK);

		board.putPieceAt(rook1, makeCoordinate(8, 1));
		board.putPieceAt(rook2, makeCoordinate(8, 8));
		board.putPieceAt(king, makeCoordinate(8, 5));
		
		board.putPieceAt(factory.makePiece(WHITEKNIGHT), makeCoordinate(8, 4));
		board.putPieceAt(factory.makePiece(BLACKKNIGHT), makeCoordinate(8, 6));
		
		assertFalse(rook1.hasMoved());
		assertFalse(rook2.hasMoved());
		assertFalse(king.hasMoved());

		assertFalse(king.canMove(makeCoordinate(8, 5), makeCoordinate(8, 3), board));
		assertFalse(king.canMove(makeCoordinate(8, 5), makeCoordinate(8, 7), board));
	}
	
	@Test //Test #38
	public void testDifferentColorsCanNotCastle() {
		ChessPiece rook1 = factory.makePiece(WHITEROOK);
		ChessPiece king = factory.makePiece(BLACKKING);
		ChessPiece rook2 = factory.makePiece(WHITEROOK);

		board.putPieceAt(rook1, makeCoordinate(1, 1));
		board.putPieceAt(rook2, makeCoordinate(1, 8));
		board.putPieceAt(king, makeCoordinate(1, 5));
		
		assertFalse(rook1.hasMoved());
		assertFalse(rook2.hasMoved());
		assertFalse(king.hasMoved());

		assertFalse(king.canMove(makeCoordinate(1, 5), makeCoordinate(1, 3), board));
		assertFalse(king.canMove(makeCoordinate(1, 5), makeCoordinate(1, 7), board));
	}
	
	
	
	
}
