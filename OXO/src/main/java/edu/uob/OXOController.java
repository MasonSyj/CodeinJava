package edu.uob;

import edu.uob.OXOMoveException.*;


public class OXOController {
    OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {

        int len = command.length();
        if (len != 2){
            throw new InvalidIdentifierLengthException(len);
        }

        int currentRow = Character.toLowerCase(command.charAt(0)) - 'a';
        int currentCol = command.charAt(1) - '1';

        if (!Character.isLetter(command.charAt(0))){
            throw new InvalidIdentifierCharacterException(OXOMoveException.RowOrColumn.ROW, command.charAt(0));
        }else if (!Character.isDigit(command.charAt(1))) {
            throw new InvalidIdentifierCharacterException(OXOMoveException.RowOrColumn.COLUMN, command.charAt(1));
        }else if (currentRow < 0 || currentRow >= gameModel.getNumberOfRows()){
            throw new OutsideCellRangeException(OXOMoveException.RowOrColumn.ROW, currentRow);
        }else if (currentCol < 0 || currentCol >= gameModel.getNumberOfColumns()){
            throw new OutsideCellRangeException(OXOMoveException.RowOrColumn.COLUMN, currentCol);
        }else if (gameModel.getCellOwner(currentRow, currentCol) != null){
            throw new CellAlreadyTakenException(currentRow, currentCol);
        }

        if (gameModel.getCellOwner(currentRow, currentCol) == null){
            gameModel.setCellOwner(currentRow, currentCol,gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));

            winDect(currentRow, currentCol);

            if(gameModel.getWinner() != null){
                return;
            }

            gameModel.setCurrentPlayerNumber((gameModel.getCurrentPlayerNumber() + 1) % gameModel.getNumberOfPlayers());
        }

        for (int j = 0; j < gameModel.getNumberOfRows(); j++){
            for (int i = 0; i < gameModel.getNumberOfColumns(); i++){
                if (gameModel.getCellOwner(j, i) == null){
                    return;
                }
            }
        }
        gameModel.setGameDrawn();

    }

    public void addRow() {
        if(gameModel.getNumberOfRows() == 9){
            return;
        }

        gameModel.addRow();
    }
    public void removeRow() {
        if (gameModel.getNumberOfRows() == 1){
            return;
        }

        for (int i = 0; i < gameModel.getNumberOfColumns(); i++){
            if (gameModel.getCellOwner(gameModel.getNumberOfRows() - 1, i) != null){
                return;
            }
        }

        boolean existnull = false;
        for (int j = 0; j < gameModel.getNumberOfRows() - 1; j++){
            for (int i = 0; i < gameModel.getNumberOfColumns(); i++){
                if (gameModel.getCellOwner(j, i) == null){
                    existnull = true;
                }
            }
        }
        if (existnull == false){
            return;
        }


        gameModel.removeRow();
    }
    public void addColumn() {
        if(gameModel.getNumberOfColumns() == 9){
            return;
        }

        gameModel.addColumn();
    }
    public void removeColumn() {
        for (int j = 0 ; j < gameModel.getNumberOfRows(); j++){
            if (gameModel.getCellOwner(j, gameModel.getNumberOfColumns() - 1)!= null){
                return;
            }
        }

        boolean existnull = false;
        for (int j = 0; j < gameModel.getNumberOfRows(); j++){
            for (int i = 0; i < gameModel.getNumberOfColumns() - 1; i++){
                if (gameModel.getCellOwner(j, i) == null){
                    existnull = true;
                }
            }
        }
        if(existnull == false){
            return;
        }

        if (gameModel.getNumberOfColumns() == 1){
            return;
        }

        gameModel.removeColumn();

    }
    public void increaseWinThreshold() {
        gameModel.setWinThreshold(gameModel.getWinThreshold() + 1);
    }

    public void decreaseWinThreshold() {
        if (gameModel.getWinner() != null && gameModel.getWinThreshold() > 3){
            gameModel.setWinThreshold(gameModel.getWinThreshold() - 1);
            return;
        }

        if (gameModel.getWinThreshold() == 3){
            return;
        }

        for (int j = 0; j < gameModel.getNumberOfRows(); j++){
            for (int i = 0; i < gameModel.getNumberOfColumns(); i++){
                if (gameModel.getCellOwner(j, i) != null){
                    return;
                }
            }
        }

        gameModel.setWinThreshold(gameModel.getWinThreshold() - 1);
    }

    public void reset() {
        gameModel.reset();
    }

    public void winDect(int currentRow, int currentCol){
        int[] dY = {0, 1, 1, 1};
        int[] dX = {1, 0, 1, -1};
        int row = gameModel.getNumberOfRows();
        int col = gameModel.getNumberOfColumns();

        for (int loop = 0; loop < 4; loop++){
            int left = 0, right = 0, y = currentRow, x = currentCol;

            for (; y >= 0 && y < row && x >= 0 && x < col; y = y + dY[loop], x = x + dX[loop]){
                if (gameModel.getCellOwner(y, x) == gameModel.getCellOwner(currentRow, currentCol)){
                    right++;
                }else{
                    break;
                }
            }
            y = currentRow;
            x = currentCol;

            for (; y >= 0 && y < row && x >= 0 && x < col; y = y - dY[loop], x = x - dX[loop]){
                if (gameModel.getCellOwner(y, x) == gameModel.getCellOwner(currentRow, currentCol)){
                    right++;
                }else{
                    break;
                }
            }

            if (right - left - 1 == gameModel.getWinThreshold()){
                gameModel.setWinner(gameModel.getCellOwner(currentRow, currentCol));
                return;
            }
        }
    }
}
