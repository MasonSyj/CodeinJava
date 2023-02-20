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
<<<<<<< HEAD

        System.out.println(currentRow + " " + currentCol);

=======
        // delete before submitting.
        System.out.println(currentRow + " " + currentCol);

        //should this stuff be part of the OXOModel?
>>>>>>> b2137a824b647285daf3676805df6342647895d4
        if (gameModel.getCellOwner(currentRow, currentCol) == null){
            gameModel.setCellOwner(currentRow, currentCol,gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));

            winDect(currentRow, currentCol);

<<<<<<< HEAD
=======
//            HorizontalVertical(currentRow, currentCol);
//            Diagonal(currentRow, currentCol);

//            winDectHorizontal(currentRow, currentCol);
//            winDectVertical(currentRow, currentCol);
//            winDectdiagonal1(currentRow, currentCol);
//            winDectdiagonal2(currentRow, currentCol);
>>>>>>> b2137a824b647285daf3676805df6342647895d4
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
        if (gameModel.getWinThreshold() >= Math.max(gameModel.getNumberOfColumns(), gameModel.getNumberOfRows())){
            return;
        }
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
            if (gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()).getPlayingLetter() == letter){
                letter++;
                i = -1;
            }
        }
        gameModel.addPlayer(new OXOPlayer(letter));
    }

    public void decreasePlayer(){

    }
    public void reset() {
        gameModel.reset();
    }

    public boolean winDect(ArrayList<OXOPlayer> list, int curPlayerIndex) {
        int left, right;
        left = right = curPlayerIndex;
        System.out.println("size " + list.size());
        while (left >= 0 && list.get(left) == list.get(curPlayerIndex)) {
            left--;
        }

        while (right < list.size() && list.get(right) == list.get(curPlayerIndex)) {
            right++;
        }

        if (right - left - 1 == gameModel.getWinThreshold()){
            gameModel.setWinner(list.get(curPlayerIndex));
            return true;
        }else{
            return false;
        }
    }

    public void HorizontalVertical(int currentRow, int currentCol){
        ArrayList<OXOPlayer> list = new ArrayList<OXOPlayer>();
        for (int i = 0; i < gameModel.getNumberOfColumns(); i++){
            list.add(gameModel.getCellOwner(currentRow, i));
        }

        if (winDect(list, currentCol)){
            return;
        }

        ArrayList<OXOPlayer> list2 = new ArrayList<OXOPlayer>();

        for (int j = 0; j < gameModel.getNumberOfRows(); j++){
            list2.add(gameModel.getCellOwner(j, currentCol));
        }

        winDect(list2, currentCol);
    }

    public void Diagonal(int currentRow, int currentCol){
        int x1, x2, y1, y2;
        y1 = y2 = currentRow;
        x1 = x2 = currentCol;
        OXOPlayer cur = gameModel.getCellOwner(currentRow, currentCol);

        for (int loop = 1; loop > -2; loop = loop - 2) {
            while (y1 >= 0 && x1 >= 0 && x1 < gameModel.getNumberOfColumns() && gameModel.getCellOwner(y1, x1) == cur){
                y1--;
                x1 += loop;
            }


            while (y2 < gameModel.getNumberOfRows() && x2 >= 0 && x2 < gameModel.getNumberOfColumns() && gameModel.getCellOwner(y2, x2) == cur){
                y2++;
                x2 += loop;
            }

            if (y2 - y1 - 1 == gameModel.getWinThreshold()){
                gameModel.setWinner(cur);
            }

            y1 = y2 = currentRow;
            x1 = x2 = currentCol;
        }


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

//    public void winDectdiagonal1(int currentRow, int currentCol){
//        int x1, x2, y1, y2;
//        y1 = y2 = currentRow;
//        x1 = x2 = currentCol;
//        OXOPlayer cur = gameModel.getCellOwner(currentRow, currentCol);
//
//        while (y1 >= 0 && x1 >= 0 && gameModel.getCellOwner(y1, x1) == cur){
//            y1--;
//            x1--;
//        }
//
//
//        while (y2 < gameModel.getNumberOfRows() && x2 < gameModel.getNumberOfColumns() && gameModel.getCellOwner(y2, x2) == cur){
//            y2++;
//            x2++;
//        }
//
//        if (y2 - y1 - 1 == gameModel.getWinThreshold()){
//            gameModel.setWinner(cur);
//        }
//    }
//
//    public void winDectdiagonal2(int currentRow, int currentCol){
//        int x1, x2, y1, y2;
//        y1 = y2 = currentRow;
//        x1 = x2 = currentCol;
//        OXOPlayer cur = gameModel.getCellOwner(currentRow, currentCol);
//
//        while (y1 >= 0 && x1 < gameModel.getNumberOfColumns() && gameModel.getCellOwner(y1, x1) == cur){
//            y1--;
//            x1++;
//        }
//
//
//        while (y2 < gameModel.getNumberOfRows() && x2 >= 0 && gameModel.getCellOwner(y2, x2) == cur){
//            y2++;
//            x2--;
//        }
//
//        if (y2 - y1 - 1 == gameModel.getWinThreshold()){
//            gameModel.setWinner(cur);
//        }


