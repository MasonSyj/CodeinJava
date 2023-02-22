package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class OXOModel {

    private List<List<OXOPlayer>> cells;
    private List<OXOPlayer> players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    private int rounds;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        cells = new ArrayList<List<OXOPlayer>>(numberOfRows);
        for (int j = 0; j < numberOfRows; j++) {
            cells.add(new ArrayList<>(numberOfColumns));
            for (int i = 0; i < numberOfColumns; i++){
                cells.get(j).add(null);
            }
        }
        players = new ArrayList<OXOPlayer>(2);

        winThreshold = winThresh;

        rounds = 0;

    }

    public int getRounds(){
        return this.rounds;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void addPlayer(OXOPlayer player) {
        players.add(player);
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players.get(number);
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
        if (winner != null){
            return;
        }
        cells.get(rowNumber).set(colNumber,player);
        rounds++;
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

    public void addRow() {
        if(getNumberOfRows() == 9){
            return;
        }

        List<OXOPlayer> temp = new ArrayList<OXOPlayer>();
        for (int i = 0; i < getNumberOfColumns(); i++){
            temp.add(null);
        }
        cells.add(temp);
    }

    public void removeRow() {

        if (getNumberOfRows() == 1){
            return;
        }

        for (int i = 0; i < getNumberOfColumns(); i++){
            if (cells.get(getNumberOfRows() - 1).get(i) != null){
                return;
            }
        }

        System.out.println("current row is" + getNumberOfRows() + " it will be deducted");

        cells.remove(getNumberOfRows() - 1);
    }

    public void addColumn() {

        if(getNumberOfColumns() == 9){
            return;
        }

        for (int j = 0; j < getNumberOfRows(); j++){
            cells.get(j).add(null);
        }
    }

    public void removeColumn() {

        if (getNumberOfColumns() == 1){
            return;
        }

        for (int j = 0 ; j < getNumberOfRows(); j++){
            if (cells.get(j).get(getNumberOfColumns() - 1) != null){
                return;
            }
        }

        for (int j = 0 ; j < getNumberOfRows(); j++){
            cells.get(j).remove(cells.get(j).size() - 1);
        }
    }

    public void reset(){
        setCurrentPlayerNumber(0);
        setWinner(null);
        this.gameDrawn = false;


        for (int j = 0; j < getNumberOfRows(); j++){
            for (int i = 0; i < getNumberOfColumns(); i++){
                setCellOwner(j, i, null);
            }
        }

        rounds = 0;

    }
}
