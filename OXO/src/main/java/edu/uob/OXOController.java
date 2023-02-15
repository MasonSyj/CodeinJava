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
    public void addRow() {
        gameModel.setCellOwner(gameModel.getNumberOfRows() + 1, gameModel.getNumberOfColumns(), null);
    }
    public void removeRow() {
        gameModel.setCellOwner(-1, gameModel.getNumberOfColumns(), null);
    }
    public void addColumn() {
        gameModel.setCellOwner(gameModel.getNumberOfRows(), gameModel.getNumberOfColumns() + 1, null);
    }
    public void removeColumn() {
        gameModel.setCellOwner(gameModel.getNumberOfRows() , -1, null);

    }
    public void increaseWinThreshold() {}
    public void decreaseWinThreshold() {}
    public void reset() {}
}
