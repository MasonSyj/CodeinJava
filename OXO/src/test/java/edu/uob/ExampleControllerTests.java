package edu.uob;

import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ExampleControllerTests {
  private OXOModel model;
  private OXOController controller;

  // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
  // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
  @BeforeEach
  void setup() {
    model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    controller = new OXOController(model);
  }

  // This next method is a utility function that can be used by any of the test methods to _safely_ send a command to the controller
  void sendCommandToController(String command) {
      // Try to send a command to the server - call will timeout if it takes too long (in case the server enters an infinite loop)
      // Note: this is ugly code and includes syntax that you haven't encountered yet
      String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
      assertTimeoutPreemptively(Duration.ofMillis(1000), ()-> controller.handleIncomingCommand(command), timeoutComment);
  }

  // Test simple move taking and cell claiming functionality
  @Test
  void testBasicMoveTaking() throws OXOMoveException {
    // Find out which player is going to make the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Make a move
    sendCommandToController("a1");

    // Check that A1 (cell [0,0] on the board) is now "owned" by the first player
    String failedTestComment = "Cell a1 wasn't claimed by the first player";
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0), failedTestComment);

    OXOPlayer seconcMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("b2");
    String failedTestCommentSecond = "Cell b2 wasn't claimed by the second player";
    assertEquals(seconcMovingPlayer, controller.gameModel.getCellOwner(1, 1), failedTestComment);

    OXOPlayer thirdMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("c3");
    String failedTestCommentThird = "Cell c3 wasn't claimed by the second player";
    assertEquals(thirdMovingPlayer, controller.gameModel.getCellOwner(2, 2), failedTestComment);


  }

  // Test out basic win detection
  @Test
  void testBasicWin() throws OXOMoveException {
    // Find out which player is going to make the first move (they should be the eventual winner)
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b3"); // First player

    // a1, a2, a3 should be a win for the first player (since players alternate between moves)
    // Let's check to see whether the first moving player is indeed the winner
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testWinHoriztontal() throws OXOMoveException {
    // Find out which player is going to make the first move (they should be the eventual winner)
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

    //make the board 7 * 7, and win threshold is 7
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();

    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b3"); // First player
    sendCommandToController("a4"); // First player
    sendCommandToController("b4"); // First player
    sendCommandToController("a5"); // First player
    sendCommandToController("b5"); // First player
    sendCommandToController("a6"); // First player
    sendCommandToController("b6"); // First player
    sendCommandToController("a7"); // First player
    sendCommandToController("b7"); // First player

    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testWinVertical() throws OXOMoveException {
    // Find out which player is going to make the first move (they should be the eventual winner)
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

    //make the board 7 * 7, and win threshold is 7
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();

    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("a5"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("b5"); // Second player
    sendCommandToController("c1"); // First player
    sendCommandToController("c5"); // First player
    sendCommandToController("d1"); // First player
    sendCommandToController("d5"); // First player
    sendCommandToController("e1"); // First player
    sendCommandToController("e5"); // First player
    sendCommandToController("f1"); // First player
    sendCommandToController("f5"); // First player
    sendCommandToController("g1"); // First player
    sendCommandToController("g5"); // First player

    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testWinDiagonal() throws OXOMoveException {
    // Find out which player is going to make the first move (they should be the eventual winner)
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

    //make the board 7 * 7, and win threshold is 7
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();

    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("a5"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("b5"); // Second player
    sendCommandToController("c3"); // First player
    sendCommandToController("c5"); // First player
    sendCommandToController("d4"); // First player
    sendCommandToController("d5"); // First player
    sendCommandToController("e5"); // First player
    sendCommandToController("e4"); // First player
    sendCommandToController("f6"); // First player
    sendCommandToController("f5"); // First player
    sendCommandToController("g7"); // First player
    sendCommandToController("g5"); // First player

    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);

    controller.reset();

    sendCommandToController("a7"); // First player
    sendCommandToController("a5"); // Second player
    sendCommandToController("b6"); // First player
    sendCommandToController("b5"); // Second player
    sendCommandToController("c5"); // First player
    sendCommandToController("c7"); // First player
    sendCommandToController("d4"); // First player
    sendCommandToController("d5"); // First player
    sendCommandToController("e3"); // First player
    sendCommandToController("e4"); // First player
    sendCommandToController("f2"); // First player
    sendCommandToController("f5"); // First player
    sendCommandToController("g1"); // First player
    sendCommandToController("g5"); // First player

    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);

  }

  @Test
  void testWinDrawn() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    controller.increaseWinThreshold();

    sendCommandToController("g3");
    sendCommandToController("c4");
    sendCommandToController("a6");
    sendCommandToController("a4");
    sendCommandToController("e2");
    sendCommandToController("a7");
    sendCommandToController("d3");
    sendCommandToController("b5");
    sendCommandToController("g2");
    sendCommandToController("c3");
    sendCommandToController("f4");
    sendCommandToController("f7");
    sendCommandToController("f1");
    sendCommandToController("g7");
    sendCommandToController("d4");
    sendCommandToController("b2");
    sendCommandToController("d5");
    sendCommandToController("c6");
    sendCommandToController("b6");
    sendCommandToController("g4");
    sendCommandToController("g1");
    sendCommandToController("a2");
    sendCommandToController("e1");
    sendCommandToController("g5");
    sendCommandToController("d1");
    sendCommandToController("f6");
    sendCommandToController("d6");
    sendCommandToController("d2");
    sendCommandToController("f5");
    sendCommandToController("e5");
    sendCommandToController("e7");
    sendCommandToController("a1");
    sendCommandToController("b4");
    sendCommandToController("d7");
    sendCommandToController("a3");
    sendCommandToController("a5");
    sendCommandToController("c2");
    sendCommandToController("e4");
    sendCommandToController("g6");
    sendCommandToController("b3");
    sendCommandToController("f2");
    sendCommandToController("e6");
    sendCommandToController("b7");
    sendCommandToController("c5");
    sendCommandToController("c1");
    sendCommandToController("f3");
    sendCommandToController("c7");
    sendCommandToController("b1");
    sendCommandToController("e3");

    String failedTestComment = "Should be Drawn by now, but it isn't.";
    assertEquals(true, model.isGameDrawn(), failedTestComment);

  }

  @Test
  void testAddRemove() throws OXOMoveException {
    String failTestComment0 = "Should be only one row, but it isn't";
    controller.removeRow();
    controller.removeRow();
    controller.removeRow();
    assertEquals(1, model.getNumberOfRows(), failTestComment0);
    controller.removeRow();
    assertEquals(1, model.getNumberOfRows(), failTestComment0);

    controller.addRow();
    controller.addRow();
    controller.addRow();

    String failTestComment_1 = "Should be only one col, but it isn't";
    controller.removeColumn();
    controller.removeColumn();
    controller.removeColumn();
    assertEquals(1, model.getNumberOfColumns(), failTestComment_1);
    controller.removeColumn();
    assertEquals(1, model.getNumberOfColumns(), failTestComment_1);
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();

    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    String failedTestComment = "Should have 9 columns, but it isn't";
    assertEquals(9, model.getNumberOfColumns(), failedTestComment);

    controller.addColumn();
    controller.addColumn();
    assertEquals(9, model.getNumberOfColumns(), failedTestComment);

    controller.removeColumn();
    controller.removeColumn();
    String failedTestComment2 = "Should have 7 columns, but it isn't";
    assertEquals(7, model.getNumberOfColumns(), failedTestComment2);

    sendCommandToController("a1");
    controller.removeColumn();
    String failedTestComment3 = "Should have 6 columns, but it isn't";
    assertEquals(6, model.getNumberOfColumns(), failedTestComment3);

    sendCommandToController("a6");
    controller.removeColumn();
    assertEquals(6, model.getNumberOfColumns(), failedTestComment3);

    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    assertEquals(9, model.getNumberOfColumns(), failedTestComment);

    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    String failedTestComment4 = "Should have 9 rows, but it isn't";
    assertEquals(9, model.getNumberOfRows(), failedTestComment4);

    controller.removeRow();
    String failedTestComment5 = "Should have 8 rows, but it isn't";
    assertEquals(8, model.getNumberOfRows(), failedTestComment5);
    sendCommandToController("h1");
    controller.removeRow();
    assertEquals(8, model.getNumberOfRows(), failedTestComment5);
  }

  @Test
  void testIncreasePlayer() throws OXOMoveException{
    controller.increasePlayer();
    controller.increasePlayer();
    controller.increasePlayer();
    String failedTestComment = "Should have 5 players, but it isn't";
    assertEquals(5, model.getNumberOfPlayers(), failedTestComment);

    String failedTestComment1 = "Should have 6 players, but it isn't";
    model.addPlayer(new OXOPlayer('D'));
    assertEquals(6, model.getNumberOfPlayers(), failedTestComment1);


  }

  @Test
  void testIncreDreacaseWinthreshold() throws OXOMoveException{

    controller.increaseWinThreshold();
    controller.increaseWinThreshold();
    String failedTestComment = "WinThreshold should be 5, but it isn't";
    assertEquals(5, model.getWinThreshold(), failedTestComment);

    controller.decreaseWinThreshold();
    controller.decreaseWinThreshold();
    controller.decreaseWinThreshold();

    String failedTestComment2 = "WinThreshold should be 3, but it isn't";
    assertEquals(3, model.getWinThreshold(), failedTestComment2);

    sendCommandToController("a3");
    controller.decreaseWinThreshold();
    assertEquals(3, model.getWinThreshold(), failedTestComment2);



  }

  // Example of how to test for the throwing of exceptions
  @Test
  void testInvalidIdentifierException() throws OXOMoveException {
    // Check that the controller throws a suitable exception when it gets an invalid command
    String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command";
    // The next lins is a bit ugly, but it is the easiest way to test exceptions (soz)
    String first = "abc123";
    assertThrows(InvalidIdentifierLengthException.class, ()-> sendCommandToController(first), failedTestComment + first);
    String second = "a";
    assertThrows(InvalidIdentifierLengthException.class, ()-> sendCommandToController(second), failedTestComment + second);
    String third = "2";
    assertThrows(InvalidIdentifierLengthException.class, ()-> sendCommandToController(third), failedTestComment + third);
    String fourth = "22";
    assertThrows(InvalidIdentifierCharacterException.class, ()-> sendCommandToController(fourth), failedTestComment + fourth);
    String fifth = "2e";
    assertThrows(InvalidIdentifierCharacterException.class, ()-> sendCommandToController(fifth), failedTestComment + fifth);
    String sixth = "a0";
    assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(sixth), failedTestComment + third);
    String seventh = "z2";
    assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(seventh), failedTestComment + seventh);

    sendCommandToController("a1");
    String eigth = "a1";
    assertThrows(CellAlreadyTakenException.class, ()-> sendCommandToController(eigth), failedTestComment + eigth);

    String ninth = "aa";
    assertThrows(InvalidIdentifierCharacterException.class, ()-> sendCommandToController(ninth), failedTestComment + ninth);

  }
}
