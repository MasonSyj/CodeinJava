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
            winDectHorizontal(currentRow, currentCol);
            winDectVertical(currentRow, currentCol);
            winDectdiagonal1(currentRow, currentCol);
            winDectdiagonal2(currentRow, currentCol);
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
    public void increaseWinThreshold() {
        gameModel.setWinThreshold(gameModel.getWinThreshold() + 1);
    }
    public void decreaseWinThreshold() {
        gameModel.setWinThreshold(gameModel.getWinThreshold() - 1);
    }
    public void reset() {}

    public void winDectHorizontal(int currentRow, int currentCol){
        int left, right;
        left = right = currentCol;
        OXOPlayer cur = gameModel.getCellOwner(currentRow, currentCol);

        while (left >= 0 && gameModel.getCellOwner(currentRow, left) == cur){
            left--;
        }

        while (right < gameModel.getNumberOfColumns() && gameModel.getCellOwner(currentRow, right) == cur){
            right++;
        }

        if (right - left - 1 == gameModel.getWinThreshold()){
            gameModel.setWinner(cur);
        }
    }

    public void winDectVertical(int currentRow, int currentCol){
        int up, down;
        up = down = currentCol;
        OXOPlayer cur = gameModel.getCellOwner(currentRow, currentCol);

        while (up >= 0 && gameModel.getCellOwner(up, currentCol) == cur){
            up--;
        }

        while (down < gameModel.getNumberOfColumns() && gameModel.getCellOwner(down, currentCol) == cur){
            down++;
        }

        if (down - up - 1 == gameModel.getWinThreshold()){
            gameModel.setWinner(cur);
        }
    }

    public void winDectdiagonal1(int currentRow, int currentCol){
        int x1, x2, y1, y2;
        y1 = y2 = currentRow;
        x1 = x2 = currentCol;
        OXOPlayer cur = gameModel.getCellOwner(currentRow, currentCol);

        while (y1 >= 0 && x1 >= 0 && gameModel.getCellOwner(y1, x1) == cur){
            y1--;
            x1--;
        }


        while (y2 < gameModel.getNumberOfRows() && x2 < gameModel.getNumberOfColumns() && gameModel.getCellOwner(y2, x2) == cur){
            y2++;
            x2++;
        }

        if (y2 - y1 - 1 == gameModel.getWinThreshold()){
            gameModel.setWinner(cur);
        }
    }

    public void winDectdiagonal2(int currentRow, int currentCol){
        int x1, x2, y1, y2;
        y1 = y2 = currentRow;
        x1 = x2 = currentCol;
        OXOPlayer cur = gameModel.getCellOwner(currentRow, currentCol);

        while (y1 >= 0 && x1 < gameModel.getNumberOfColumns() && gameModel.getCellOwner(y1, x1) == cur){
            y1--;
            x1++;
        }


        while (y2 < gameModel.getNumberOfRows() && x2 >= 0 && gameModel.getCellOwner(y2, x2) == cur){
            y2++;
            x2--;
        }

        if (y2 - y1 - 1 == gameModel.getWinThreshold()){
            gameModel.setWinner(cur);
        }
    }
}
