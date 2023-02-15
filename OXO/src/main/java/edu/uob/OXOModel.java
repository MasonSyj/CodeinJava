package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class OXOModel {

    private List<List<OXOPlayer>> cells;
    private OXOPlayer[] players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        cells = new ArrayList<List<OXOPlayer>>(numberOfRows);
        for (int j = 0; j < numberOfRows; j++) {
            cells.add(new ArrayList<>(numberOfColumns));
            for (int i = 0; i < numberOfColumns; i++){
                cells.get(j).add(null);
            }

        }
        players = new OXOPlayer[2];
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void addPlayer(OXOPlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players[number];
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        if (cells.size() < rowNumber){
            List<OXOPlayer> temp = new ArrayList<OXOPlayer>(colNumber);
            for (int i = 0; i < colNumber; i++){
                temp.add(null);
            }
            cells.add(temp);
            return;
        }

        if (rowNumber == -1){
            cells.remove(cells.size() - 1);
            return;
        }

        if (colNumber == -1){
            for (int j = 0 ; j < cells.size(); j++){
                cells.get(j).remove(cells.get(j).size() - 1);
            }
            return;
        }

        if (cells.get(0).size() < colNumber){
            for (int j = 0; j < cells.size(); j++){
                cells.get(j).add(null);
            }
            return;
        }

        cells.get(rowNumber).set(colNumber,player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

}
