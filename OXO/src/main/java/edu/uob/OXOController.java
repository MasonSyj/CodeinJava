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
        OXOPlayer[][] newcells = new OXOPlayer[gameModel.getNumberOfRows() + 1][gameModel.getNumberOfColumns()];
//        OXOModel newmodel = new OXOModel(gameModel.getNumberOfRows() + 1, gameModel.getNumberOfColumns(), 3);
        for (int j = 0; j < gameModel.getNumberOfRows(); j++){
            for (int i = 0; i < gameModel.getNumberOfColumns(); i++){
//                newmodel.setCellOwner(j, i, gameModel.getCellOwner(j, i));
                newcells[j][i] = gameModel.getCellOwner(j, i);
            }
        }
        gameModel.cells = newcells;
    }
    public void removeRow() {
        OXOPlayer[][] newcells = new OXOPlayer[gameModel.getNumberOfRows() - 1][gameModel.getNumberOfColumns()];
//        OXOModel newmodel = new OXOModel(gameModel.getNumberOfRows() + 1, gameModel.getNumberOfColumns(), 3);
        for (int j = 0; j < gameModel.getNumberOfRows() - 1; j++){
            for (int i = 0; i < gameModel.getNumberOfColumns(); i++){
//                newmodel.setCellOwner(j, i, gameModel.getCellOwner(j, i));
                newcells[j][i] = gameModel.getCellOwner(j, i);
            }
        }
        gameModel.cells = newcells;
    }
    public void addColumn() {
        OXOPlayer[][] newcells = new OXOPlayer[gameModel.getNumberOfRows()][gameModel.getNumberOfColumns() + 1];
//        OXOModel newmodel = new OXOModel(gameModel.getNumberOfRows() + 1, gameModel.getNumberOfColumns(), 3);
        for (int j = 0; j < gameModel.getNumberOfRows(); j++){
            for (int i = 0; i < gameModel.getNumberOfColumns(); i++){
//                newmodel.setCellOwner(j, i, gameModel.getCellOwner(j, i));
                newcells[j][i] = gameModel.getCellOwner(j, i);
            }
        }
        gameModel.cells = newcells;
    }
    public void removeColumn() {
        OXOPlayer[][] newcells = new OXOPlayer[gameModel.getNumberOfRows()][gameModel.getNumberOfColumns() - 1];
//        OXOModel newmodel = new OXOModel(gameModel.getNumberOfRows() + 1, gameModel.getNumberOfColumns(), 3);
        for (int j = 0; j < gameModel.getNumberOfRows(); j++){
            for (int i = 0; i < gameModel.getNumberOfColumns() - 1; i++){
//                newmodel.setCellOwner(j, i, gameModel.getCellOwner(j, i));
                newcells[j][i] = gameModel.getCellOwner(j, i);
            }
        }
        gameModel.cells = newcells;
    }
    public void increaseWinThreshold() {}
    public void decreaseWinThreshold() {}
    public void reset() {}
}
