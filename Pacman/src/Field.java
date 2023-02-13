import java.util.Random;

public class Field {
    int ypac;
    int xpac;
    int row;
    int col;
    int numFoods;
    Cell[][] board;
    int points;


    public void move(Direction dir){
        Cell temp = this.board[ypac][xpac];
        this.board[ypac][xpac] = new Cell();
        if (dir == Direction.toDown){
            ypac = (ypac + 1) % row;
        }else if (dir == Direction.toLeft){
            xpac--;
            if (xpac < 0){
                xpac = col - 1;
            }
        }else if (dir == Direction.toUp){
            ypac--;
            if (ypac < 0){
                ypac = row - 1;
            }
        }else if (dir == Direction.toRight){
            xpac = (xpac + 1) % col;
        }

        if (this.board[this.ypac][this.xpac] instanceof Marble || this.board[this.ypac][this.xpac] instanceof Pill){
            this.numFoods--;
            Food tempf = (Food)(this.board[ypac][xpac]);
            this.points += tempf.points;
        }
        this.board[ypac][xpac] = temp;
    }

    public Field(int row, int col){
        this.points = 0;
        this.row = row;
        this.col = col;
        this.ypac = this.row / 2;
        this.xpac = this.col / 2;
        Random rand = new Random();
        this.board = new Cell[row][col];
        this.numFoods = 0;
        for (int j = 0; j < this.row; j++){
            for (int i = 0; i < this.col; i++){
                if (rand.nextInt(10) < 7){
                    this.board[j][i] = new Cell();
                }else if (rand.nextInt(10) < 9){
                    this.board[j][i] = new Marble();
                    this.numFoods++;
                }else{
                    this.board[j][i] = new Pill();
                    this.numFoods++;
                }
            }
        }
        if (this.board[this.ypac][this.xpac] instanceof Marble || this.board[this.ypac][this.xpac] instanceof Pill){
            this.numFoods--;
        }
        this.board[this.ypac][this.xpac] = new Pacman();
    }
}
