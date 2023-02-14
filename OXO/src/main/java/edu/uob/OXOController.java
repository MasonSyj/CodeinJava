package edu.uob;

public class OXOController {
    OXOModel gameModel;

    int currentRow;
    int currentCol;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        int currentRow = Character.toLowerCase(command.charAt(0)) - 'a';
        int currentCol = command.charAt(1) - '1';
        System.out.println(currentRow + " " + currentCol);
        if (gameModel.getCellOwner(currentRow, currentCol) == null){
            gameModel.setCellOwner(currentRow, currentCol,gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
            gameModel.setCurrentPlayerNumber((gameModel.getCurrentPlayerNumber() + 1) % gameModel.getNumberOfPlayers());
        }
    }
    public void addRow() {}
    public void removeRow() {}
    public void addColumn() {}
    public void removeColumn() {}
    public void increaseWinThreshold() {}
    public void decreaseWinThreshold() {}
    public void reset() {}
}
