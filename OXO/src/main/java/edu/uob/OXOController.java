package edu.uob;

import edu.uob.OXOMoveException.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class OXOController {
    OXOModel gameModel;

    int currentRow;
    int currentCol;

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

        if (!Character.isLowerCase(command.charAt(0))){
            throw new InvalidIdentifierCharacterException(OXOMoveException.RowOrColumn.ROW, Character.toLowerCase(command.charAt(0)));
        }else if (!Character.isDigit(command.charAt(1))) {
            throw new InvalidIdentifierCharacterException(OXOMoveException.RowOrColumn.COLUMN, command.charAt(1));
        }else if (currentRow < 0 || currentRow >= gameModel.getNumberOfRows()){
            throw new OutsideCellRangeException(OXOMoveException.RowOrColumn.ROW, currentRow);
        }else if (currentCol < 0 || currentCol >= gameModel.getNumberOfColumns()){
            throw new OutsideCellRangeException(OXOMoveException.RowOrColumn.COLUMN, currentCol);
        }else if (gameModel.getCellOwner(currentRow, currentCol) != null){
            throw new CellAlreadyTakenException(currentRow, currentCol);
        }

        // delete before submitting.
        System.out.println(currentRow + " " + currentCol);

        if (gameModel.getCellOwner(currentRow, currentCol) == null){
            gameModel.setCellOwner(currentRow, currentCol,gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));

            winDect(currentRow, currentCol);

            gameModel.setCurrentPlayerNumber((gameModel.getCurrentPlayerNumber() + 1) % gameModel.getNumberOfPlayers());
        }

        if (gameModel.getRounds() == gameModel.getNumberOfRows() * gameModel.getNumberOfColumns()){
            gameModel.setGameDrawn();
        }
    }

    public void addRow() {
        gameModel.addRow();
    }
    public void removeRow() {
        gameModel.removeRow();
    }
    public void addColumn() {
        gameModel.addColumn();
    }
    public void removeColumn() {
        gameModel.removeColumn();

    }
    public void increaseWinThreshold() {

        gameModel.setWinThreshold(gameModel.getWinThreshold() + 1);
        System.out.println("new Threshold: " + gameModel.getWinThreshold());

    }
    public void decreaseWinThreshold() {
        if (gameModel.getWinThreshold() == 3 || gameModel.getRounds() > 0){
            return;
        }
        gameModel.setWinThreshold(gameModel.getWinThreshold() - 1);
        System.out.println("new Threshold: " + gameModel.getWinThreshold());
    }

    public void increasePlayer(){
        Character letter = 'A';
        for (int i = 0; i < gameModel.getNumberOfPlayers(); i++){
            if (gameModel.getPlayerByNumber(i).getPlayingLetter() == letter){
                letter++;
                i = -1;
            }
        }
        gameModel.addPlayer(new OXOPlayer(letter));
    }



    public void reset() {
        gameModel.reset();
    }


    public void winDect(int currentRow, int currentCol){
        int[] dY = {0, 1, 1, 1};
        int[] dX = {1, 0, 1, -1};

        for (int loop = 0; loop < 4; loop++){
            int left = 0, right = 0, y = currentRow, x = currentCol;

            for (; y >= 0 && y < gameModel.getNumberOfRows() && x >= 0 && x < gameModel.getNumberOfColumns(); y = y + dY[loop], x = x + dX[loop]){
                if (gameModel.getCellOwner(y, x) == gameModel.getCellOwner(currentRow, currentCol)){
                    right++;
                }
            }
            y = currentRow;
            x = currentCol;

            for (; y >= 0 && y < gameModel.getNumberOfRows() && x >= 0 && x < gameModel.getNumberOfColumns(); y = y - dY[loop], x = x - dX[loop]){
                if (gameModel.getCellOwner(y, x) == gameModel.getCellOwner(currentRow, currentCol)){
                    right++;
                }
            }

            if (right - left - 1 == gameModel.getWinThreshold()){
                gameModel.setWinner(gameModel.getCellOwner(currentRow, currentCol));
                return;
            }
        }
    }

}
