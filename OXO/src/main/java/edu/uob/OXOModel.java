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
<<<<<<< HEAD
        rounds = 0;
=======
>>>>>>> b2137a824b647285daf3676805df6342647895d4
    }

    public int getRounds(){return this.rounds;}

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
<<<<<<< HEAD
        if(cells.size() == 9){
            return;
        }
=======
>>>>>>> b2137a824b647285daf3676805df6342647895d4
        List<OXOPlayer> temp = new ArrayList<OXOPlayer>();
        for (int i = 0; i < getNumberOfColumns(); i++){
            temp.add(null);
        }
        cells.add(temp);
    }

    public void removeRow() {
<<<<<<< HEAD
        if (getNumberOfRows() == 1){
=======
        System.out.println("current row is" + getNumberOfRows() + " it will be deducted");
        if (getNumberOfRows() == 0){
>>>>>>> b2137a824b647285daf3676805df6342647895d4
            return;
        }
        cells.remove(getNumberOfRows() - 1);
    }

    public void addColumn() {
<<<<<<< HEAD
        if(cells.get(0).size() == 9){
            return;
        }

=======
>>>>>>> b2137a824b647285daf3676805df6342647895d4
        for (int j = 0; j < cells.size(); j++){
            cells.get(j).add(null);
        }
    }

    public void removeColumn() {
<<<<<<< HEAD
        if (cells.get(0).size() == 1){
            return;
        }
=======
>>>>>>> b2137a824b647285daf3676805df6342647895d4
        for (int j = 0 ; j < cells.size(); j++){
            cells.get(j).remove(cells.get(j).size() - 1);
        }
    }

    public void reset(){
        for (int j = 0; j < cells.size(); j++){
            for (int i = 0; i < cells.get(0).size(); i++){
                setCellOwner(j, i, null);
            }
        }
<<<<<<< HEAD
        setCurrentPlayerNumber(0);
        setWinner(null);
        this.gameDrawn = false;
=======
        setCurrentPlayerNumber(getNumberOfPlayers() + 1);
>>>>>>> b2137a824b647285daf3676805df6342647895d4
    }
}
