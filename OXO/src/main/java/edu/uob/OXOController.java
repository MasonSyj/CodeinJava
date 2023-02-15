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
        boolean win = true;
        for (int cnt = 0; cnt < gameModel.getWinThreshold() - 1; cnt++){
            if (currentCol + cnt + 1 >= gameModel.getNumberOfColumns() ||
                    gameModel.getCellOwner(currentRow, currentCol + cnt + 1) == null
            || gameModel.getCellOwner(currentRow, currentCol + cnt) != gameModel.getCellOwner(currentRow, currentCol + cnt + 1)){
                win = false;
                break;
            }
        }
        if (win == true){
            gameModel.setWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
            System.out.println("win");
            return;
        }
        win = true;
        for (int cnt = 0; cnt < gameModel.getWinThreshold() - 1; cnt++){
            if (currentCol - cnt - 1 < 0 ||
                    gameModel.getCellOwner(currentRow, currentCol - cnt - 1) == null
                    || gameModel.getCellOwner(currentRow, currentCol - cnt) != gameModel.getCellOwner(currentRow, currentCol - cnt - 1)){
                win = false;
                break;
            }
        }
        if (win == true){
            gameModel.setWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
            System.out.println("win");
            return;
        }
        System.out.println("no winner");
    }

    public void winDectVertical(int currentRow, int currentCol){
        boolean win = true;
        for (int cnt = 0; cnt < gameModel.getWinThreshold() - 1; cnt++){
            if (currentRow + cnt + 1 >= gameModel.getNumberOfRows() ||
                    gameModel.getCellOwner(currentRow + cnt + 1, currentCol) == null
                    || gameModel.getCellOwner(currentRow + cnt, currentCol) != gameModel.getCellOwner(currentRow + cnt + 1, currentCol)){
                win = false;
                break;
            }
        }
        if (win == true){
            gameModel.setWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
            System.out.println("win");
            return;
        }
        win = true;
        for (int cnt = 0; cnt < gameModel.getWinThreshold() - 1; cnt++){
            if (currentRow - cnt - 1 < 0 ||
                    gameModel.getCellOwner(currentRow - cnt - 1, currentCol) == null
                    || gameModel.getCellOwner(currentRow - cnt, currentCol) != gameModel.getCellOwner(currentRow - cnt - 1, currentCol)){
                win = false;
                break;
            }
        }
        if (win == true){
            gameModel.setWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
            System.out.println("win");
            return;
        }
        System.out.println("no winner");
    }
}
