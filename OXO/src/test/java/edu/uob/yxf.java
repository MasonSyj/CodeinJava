package edu.uob;

import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class yxf {
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



  // ------------------------------------------------------------
  // --------------------Test Input Command----------------------
  // ------------------------------------------------------------
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
  }

  // Test lower-case and upper-case input command both work correctly
  @Test
  void testIncomingCommand() throws OXOMoveException  {
    OXOPlayer XPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    OXOPlayer OPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber()+1);

    sendCommandToController("A1"); // X
    String failedTestComment = "Cell owner was expected to be "+ XPlayer.getPlayingLetter() +" but wasn't";
    assertEquals(XPlayer,model.getCellOwner(0,0),failedTestComment);

    sendCommandToController("b2"); // O
    failedTestComment = "Cell owner was expected to be "+ OPlayer.getPlayingLetter() +" but wasn't";
    assertEquals(OPlayer,model.getCellOwner(1,1),failedTestComment);

    sendCommandToController("C3"); // X
    failedTestComment = "Cell owner was expected to be "+ XPlayer.getPlayingLetter() +" but wasn't";
    assertEquals(XPlayer,model.getCellOwner(2,2),failedTestComment);
  }

  // Test invalid input command
  @Test
  void testExceptions() throws OXOMoveException {
    // Test Invalid input command
    assertThrows(InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("aa1"));
    assertThrows(InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("298437"));
    assertThrows(InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("()@LD)"));
    assertThrows(InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("2a"));
    assertThrows(InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("* "));
    assertThrows(InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("A*"));
    assertThrows(InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("Z/"));
    // Test command is out of board range
    assertThrows(OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("e2"));
    assertThrows(OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("a0"));
    assertThrows(OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("a9"));
    // Test Cell Already be Taken
    sendCommandToController("a1");
    sendCommandToController("b3");
    assertThrows(CellAlreadyTakenException.class, ()-> controller.handleIncomingCommand("a1"));
    assertThrows(CellAlreadyTakenException.class, ()-> controller.handleIncomingCommand("b3"));
  }



  // ------------------------------------------------------------
  // ------------Test add/remove rows/cols functions-------------
  // ------------------------------------------------------------
  @Test
  void testChangeBoardSizeLimit() throws OXOMoveException {
    // Test cols and rows cannot exceed 9
    for (int i = 0; i < 10; i++){
      controller.addRow();
    }
    String failedTestComment = "The number of rows cannot exceed 9";
    assertEquals(9,model.getNumberOfRows(),failedTestComment);
    for (int i = 0; i < 10; i++){
      controller.addColumn();
    }
    failedTestComment = "The number of column cannot exceed 9";
    assertEquals(9,model.getNumberOfColumns(),failedTestComment);

    // Test cols and rows cannot less than 1
    controller.reset();
    for (int i = 0; i < 10; i++){
      controller.removeRow();
    }
    failedTestComment = "The number of rows cannot less than 1";
    assertEquals(1,model.getNumberOfRows(),failedTestComment);
    for (int i = 0; i < 10; i++){
      controller.removeColumn();
    }
    failedTestComment = "The number of column cannot less than 1";
    assertEquals(1,model.getNumberOfColumns(),failedTestComment);
  }

  @Test
  void testChangeBoardSize_atBegin() throws OXOMoveException {
    // Can add/remove rol/col at beginning
    controller.addRow();
    String failedTestComment = "Column number was expected to be 4 but wasn't";
    assertEquals(4, model.getNumberOfRows(), failedTestComment);

    controller.addColumn();
    failedTestComment = "Column number was expected to be 4 but wasn't";
    assertEquals(4, model.getNumberOfColumns(), failedTestComment);

    controller.removeRow();
    failedTestComment = "Row number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfRows(), failedTestComment);

    controller.removeColumn();
    failedTestComment = "Column number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfColumns(), failedTestComment);
  }

  @Test
  void testChangeBoardSize_inProcess() throws OXOMoveException {
    // Can add rol/col during process
    sendCommandToController("a1");
    sendCommandToController("b1");
    controller.addRow();
    String failedTestComment = "Row number was expected to be 4 but wasn't";
    assertEquals(4, model.getNumberOfRows(), failedTestComment);
    controller.addColumn();
    failedTestComment = "Column number was expected to be 4 but wasn't";
    assertEquals(4, model.getNumberOfColumns(), failedTestComment);

    // Can remove rol/col when there is no cell owner
    controller.removeRow();
    failedTestComment = "Row number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfRows(), failedTestComment);
    controller.removeColumn();
    failedTestComment = "Column number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfColumns(), failedTestComment);

    // Can not remove rol/col when there is a cell owner
    sendCommandToController("c3"); // a cell is owned in last rol and col
    controller.removeRow();
    failedTestComment = "Row number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfRows(), failedTestComment);
    controller.removeColumn();
    failedTestComment = "Column number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfColumns(), failedTestComment);
  }

  @Test
  void testChangeBoardSize_afterDrawn_add() throws OXOMoveException {
    // Can add rol/col after drawn
    drawn_2players_3x3();
    controller.addRow();
    String failedTestComment = "Row number was expected to be 4 but wasn't";
    assertEquals(4, model.getNumberOfRows(), failedTestComment);
    controller.addColumn();
    failedTestComment = "Column number was expected to be 4 but wasn't";
    assertEquals(4, model.getNumberOfColumns(), failedTestComment);
  }

  @Test
  void testChangeBoardSize_afterDrawn_remove() throws OXOMoveException {
    // Cannot remove rol/col after drawn because all cells have owner
    drawn_2players_3x3();
    controller.removeRow();
    String failedTestComment = "Row number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfRows(), failedTestComment);
    controller.removeColumn();
    failedTestComment = "Column number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfColumns(), failedTestComment);
  }

  @Test
  void testChangeBoardSize_afterWin_add() throws OXOMoveException {
    // Can add rol/col after win
    winDiagonal_1_X();
    controller.addRow();
    String failedTestComment = "Row number was expected to be 4 but wasn't";
    assertEquals(4, model.getNumberOfRows(), failedTestComment);
    controller.addColumn();
    failedTestComment = "Column number was expected to be 4 but wasn't";
    assertEquals(4, model.getNumberOfColumns(), failedTestComment);
  }

  @Test
  void testChangeBoardSize_afterWin_remove() throws OXOMoveException {
    // Can remove rol/col only when there have no cell owners
    winHorizontal_1_X();
    controller.removeRow();
    String failedTestComment = "Row number was expected to be 2 but wasn't";
    assertEquals(2, model.getNumberOfRows(), failedTestComment);

    // Cannot remove rol/col when there have cell owners
    controller.removeColumn();
    failedTestComment = "Column number was expected to be 3 but wasn't";
    assertEquals(3, model.getNumberOfColumns(), failedTestComment);
  }

  @Test
  void testChangeBoardSize_affectDrawn() throws OXOMoveException {
    // Remove col/row to make game drawn
    sendCommandToController("a1");
    sendCommandToController("b1");
    sendCommandToController("a2");
    sendCommandToController("b2");
    controller.removeRow();
    controller.removeColumn();
    String failedTestComment = "Game was expected to be drawn but wasn't";
    assertEquals(true, model.isGameDrawn(), failedTestComment);

    // Add a row to make game not drawn
    controller.addRow();
    failedTestComment = "Game was expected to be not drawn but wasn't";
    assertEquals(false, model.isGameDrawn(), failedTestComment);
  }



  // ------------------------------------------------------------
  // ------------Test Change win threshold functions-------------
  // ------------------------------------------------------------
  @Test
  void testIncreaseWinThreshold() throws OXOMoveException {
    // Players can increase win threshold at any time
    // At beginning player can increase win threshold
    controller.increaseWinThreshold();
    String failedTestComment = "Win threshold was expected to be 4 but wasn't";
    assertEquals(4, model.getWinThreshold(), failedTestComment);

    // During process player can increase win threshold
    sendCommandToController("a1");
    sendCommandToController("b1");
    controller.increaseWinThreshold();
    failedTestComment = "Win threshold was expected to be 5 but wasn't";
    assertEquals(5, model.getWinThreshold(), failedTestComment);

    // After win players can increase win threshold
    controller.reset();
    controller.decreaseWinThreshold();
    controller.decreaseWinThreshold(); // Reset win threshold to 3
    winVertical_1_X();
    controller.increaseWinThreshold();
    failedTestComment = "Win threshold was expected to be 4 but wasn't";
    assertEquals(4, model.getWinThreshold(), failedTestComment);

    // After drawn players can increase win threshold
    controller.reset();
    controller.decreaseWinThreshold(); // Reset win threshold to 3
    drawn_2players_3x3();
    controller.increaseWinThreshold();
    failedTestComment = "Win threshold was expected to be 4 but wasn't";
    assertEquals(4, model.getWinThreshold(), failedTestComment);
  }

  @Test
  void testWinThresholdLimit() throws OXOMoveException {
    // win threshold range is [3,+∞)
    // Test win threshold can't be less than 3
    controller.decreaseWinThreshold();
    String failedTestComment = "Win threshold was expected to be 3 but wasn't";
    assertEquals(3, model.getWinThreshold(), failedTestComment);
  }

  // Players can decrease win threshold just at beginning and after win
  @Test
  void testDecreaseWinThreshold_atBegin() throws OXOMoveException {
    // At beginning player can decrease win threshold
    controller.increaseWinThreshold();
    controller.decreaseWinThreshold();
    String failedTestComment = "Win threshold was expected to be 3 but wasn't";
    assertEquals(3, model.getWinThreshold(), failedTestComment);
  }
  @Test
  void testDecreaseWinThreshold_inProcess() throws OXOMoveException {
    // During process player cannot decrease win threshold
    controller.increaseWinThreshold();
    sendCommandToController("a1");
    controller.decreaseWinThreshold();
    String failedTestComment = "Win threshold was expected to be 4 but wasn't";
    assertEquals(4, model.getWinThreshold(), failedTestComment);
  }
  @Test
  void testDecreaseWinThreshold_afterWin() throws OXOMoveException {
    // After win players can decrease win threshold
    winHorizontal_1_X();
    controller.increaseWinThreshold();
    String failedTestComment = "Win threshold was expected to be 4 but wasn't";
    assertEquals(4, model.getWinThreshold(), failedTestComment);
    controller.decreaseWinThreshold();
    failedTestComment = "Win threshold was expected to be 3 but wasn't";
    assertEquals(3, model.getWinThreshold(), failedTestComment);
  }
  @Test
  void testDecreaseWinThreshold_afterDrawn() throws OXOMoveException {
    // After drawn players cannot decrease win threshold
    controller.increaseWinThreshold();
    drawn_2players_3x3();
    controller.decreaseWinThreshold();
    String failedTestComment = "Win threshold was expected to be 4 but wasn't";
    assertEquals(4, model.getWinThreshold(), failedTestComment);
  }



  // ------------------------------------------------------------
  // -------------------Test Reset function----------------------
  // ------------------------------------------------------------
    @Test
  void testReset() throws OXOMoveException {
    winHorizontal_1_X();
    controller.reset();
    String failedTestComment = "Winner was expected to be null but wasn't";
    assertNull(model.getWinner(), failedTestComment);
    failedTestComment = "Current player was expected to be the first player but wasn't";
    assertEquals(0, model.getCurrentPlayerNumber(), failedTestComment);
    failedTestComment = "Game was expected to be not Drawn but wasn't";
    assertFalse(model.isGameDrawn(), failedTestComment);
  }

  @Test
  void testReset_boardClear() throws OXOMoveException {
    winHorizontal_1_X();
    controller.reset();
    String failedTestComment = "Cell was expected to be null but wasn't";
    for (int y = 0; y < model.getNumberOfRows(); y++){
      for (int x = 0; x < model.getNumberOfColumns(); x++){
        assertNull(model.getCellOwner(y,x), failedTestComment);
      }
    }
  }

  @Test
  void testReset_winThreshold() throws OXOMoveException {
    // Reset won't change win threshold
    controller.increaseWinThreshold();
    controller.reset();
    String failedTestComment = "Win threshold was expected to be 4 but wasn't";
    assertEquals(4,model.getWinThreshold(),failedTestComment);
  }

  @Test
  void testReset_boardSize() throws OXOMoveException {
    // Reset won't change board size (row and col)
    controller.addRow();
    controller.removeColumn();
    controller.reset();
    String failedTestComment = "Row number was expected to be 4 but wasn't";
    assertEquals(4,model.getNumberOfRows(),failedTestComment);
    failedTestComment = "Row number was expected to be 2 but wasn't";
    assertEquals(2,model.getNumberOfColumns(),failedTestComment);
  }



  // ------------------------------------------------------------
  // -------------------Test win/drawn cases---------------------
  // ------------------------------------------------------------
  @Test
  void testWin_3x3() throws OXOMoveException {
    OXOPlayer XPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    OXOPlayer OPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber()+1);

    winHorizontal_1_X();
    String failedTestComment = "Winner was expected to be " + XPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(XPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    winHorizontal_2_X();
    assertEquals(XPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    winVertical_1_X();
    assertEquals(XPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    winDiagonal_1_X();
    assertEquals(XPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    winHorizontal_3_O();
    failedTestComment = "Winner was expected to be " + OPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(OPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    winVertical_2_O();
    assertEquals(OPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    winDiagonal_2_O();
    assertEquals(OPlayer,model.getWinner(),failedTestComment);
  }

  @Test
  void testDrawn_3x3() throws OXOMoveException {
    drawn_2players_3x3();
    String failedTestComment = "Game was expected to be drawn but wasn't";
    assertEquals(true,model.isGameDrawn(),failedTestComment);
  }

  // Test win/drawn cases in rectangle board
  @Test
  void testWinDrawn_4x3_Diagonal() throws OXOMoveException {
    controller.addRow();
    OXOPlayer XPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    OXOPlayer OPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber()+1);

    String failedTestComment = "Winner was expected to be " + XPlayer.getPlayingLetter() + " but wasn't";
    winDiagonal_1_X();
    assertEquals(XPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    sendCommandToController("d1"); // X
    sendCommandToController("c1"); // O
    sendCommandToController("c2"); // X
    sendCommandToController("d2"); // O
    sendCommandToController("b3"); // X
    assertEquals(XPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    failedTestComment = "Winner was expected to be " + OPlayer.getPlayingLetter() + " but wasn't";
    winDiagonal_2_O();
    assertEquals(OPlayer,model.getWinner(),failedTestComment);

    controller.reset();
    sendCommandToController("a2"); // X
    sendCommandToController("b1"); // O
    sendCommandToController("b2"); // X
    sendCommandToController("c2"); // O
    sendCommandToController("d2"); // X
    sendCommandToController("d3"); // O
    assertEquals(OPlayer,model.getWinner(),failedTestComment);
  }

  @Test
  void testWinToNotWin() throws OXOMoveException {
    // At first win
    OXOPlayer XPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    winDiagonal_1_X();
    String failedTestComment = "Winner was expected to be " + XPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(XPlayer,model.getWinner(),failedTestComment);

    // Increase win threshold and reset
    // No winner at this situation
    controller.increaseWinThreshold();
    controller.reset();
    winDiagonal_1_X();
    failedTestComment = "Winner was expected to be null but wasn't";
    assertEquals(null,model.getWinner(),failedTestComment);
  }



  // ------------------------------------------------------------
  // -------------------Test multiple players--------------------
  // ------------------------------------------------------------
  // Test add multiple players
  @Test
  void testAddPlayer() throws OXOMoveException {
    model.addPlayer(new OXOPlayer('A'));
    String failedTestComment = "Players was expected to be 3 but wasn't";
    assertEquals(3,model.getNumberOfPlayers(), failedTestComment);
    model.addPlayer(new OXOPlayer('B'));
    failedTestComment = "Players was expected to be 4 but wasn't";
    assertEquals(4,model.getNumberOfPlayers(), failedTestComment);
  }

  @Test
  void testWinDrawn_MultiplePlayers() throws OXOMoveException {
    // 3 players in 3*3 board and the 3rd player win
    OXOPlayer thirdMovingPlayer = new OXOPlayer('Z');
    model.addPlayer(thirdMovingPlayer);
    sendCommandToController("a1"); // X
    sendCommandToController("a2"); // O
    sendCommandToController("c1"); // Z
    sendCommandToController("b1"); // X
    sendCommandToController("b3"); // O
    sendCommandToController("b2"); // Z
    sendCommandToController("c2"); // X
    sendCommandToController("c3"); // O
    sendCommandToController("a3"); // Z
    String failedTestComment = "Winner was expected to be " + thirdMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(thirdMovingPlayer, model.getWinner(), failedTestComment);

    // 4 players in 3*3 board and drawn
    controller.reset();
    OXOPlayer forthMovingPlayer = new OXOPlayer('M');
    model.addPlayer(forthMovingPlayer);
    sendCommandToController("a1"); // X
    sendCommandToController("a2"); // O
    sendCommandToController("a3"); // Z
    sendCommandToController("b1"); // M
    sendCommandToController("b3"); // X
    sendCommandToController("b2"); // O
    sendCommandToController("c2"); // Z
    sendCommandToController("c3"); // M
    sendCommandToController("c1"); // X
    failedTestComment = "Game was expected to be drawn but wasn't";
    assertEquals(true, model.isGameDrawn(), failedTestComment);

    // 4 players in 4*4 board and the second player win
    controller.reset();
    model.addRow();
    model.addColumn();
    sendCommandToController("a1"); // X
    sendCommandToController("a2"); // O
    sendCommandToController("a3"); // Z
    sendCommandToController("b1"); // M
    sendCommandToController("b3"); // X
    sendCommandToController("b2"); // O
    sendCommandToController("d3"); // Z
    sendCommandToController("c3"); // M
    sendCommandToController("d1"); // X
    sendCommandToController("c2"); // O
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(1);
    failedTestComment = "Winner was expected to be " + secondMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(secondMovingPlayer, model.getWinner(), failedTestComment);
  }



  // ------------------------------------------------------------
  // --------------------Some win/drawn cases--------------------
  // ------------------------------------------------------------
  void winHorizontal_1_X(){
    sendCommandToController("a1"); // X
    sendCommandToController("b1"); // O
    sendCommandToController("a2"); // X
    sendCommandToController("b2"); // O
    sendCommandToController("a3"); // X
  }

  void winHorizontal_2_X(){
    sendCommandToController("b1"); // X
    sendCommandToController("c1"); // O
    sendCommandToController("b2"); // X
    sendCommandToController("c2"); // O
    sendCommandToController("b3"); // X
  }

  void winHorizontal_3_O(){
    sendCommandToController("a1"); // X
    sendCommandToController("c1"); // O
    sendCommandToController("b2"); // X
    sendCommandToController("c2"); // O
    sendCommandToController("a3"); // X
    sendCommandToController("c3"); // O
  }

  void winVertical_1_X(){
    sendCommandToController("a1"); // X
    sendCommandToController("a2"); // O
    sendCommandToController("b1"); // X
    sendCommandToController("b2"); // O
    sendCommandToController("c1"); // X
  }

  void winVertical_2_O(){
    sendCommandToController("a1"); // X
    sendCommandToController("c1"); // O
    sendCommandToController("b1"); // X
    sendCommandToController("c2"); // O
    sendCommandToController("b2"); // X
    sendCommandToController("c3"); // O
  }

  void winDiagonal_1_X(){
    sendCommandToController("a1"); // X
    sendCommandToController("a2"); // O
    sendCommandToController("a3"); // X
    sendCommandToController("b1"); // O
    sendCommandToController("b2"); // X
    sendCommandToController("b3"); // O
    sendCommandToController("c3"); // X
  }

  void winDiagonal_2_O(){
    sendCommandToController("a1"); // X
    sendCommandToController("a3"); // O
    sendCommandToController("b1"); // X
    sendCommandToController("b2"); // O
    sendCommandToController("b3"); // X
    sendCommandToController("c1"); // O
  }

  void drawn_2players_3x3(){
    sendCommandToController("a1"); // X
    sendCommandToController("a2"); // O
    sendCommandToController("a3"); // X
    sendCommandToController("b1"); // O
    sendCommandToController("b3"); // X
    sendCommandToController("b2"); // O
    sendCommandToController("c1"); // X
    sendCommandToController("c3"); // O
    sendCommandToController("c2"); // X
  }

}
