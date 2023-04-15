import processing.core.PApplet;

import java.awt.geom.Arc2D;
import java.util.Random;

public class Sketch extends PApplet {

    public int doddleX;
    public int doddleY;
    public static int maxStep = 150;
    public int step;
    public Direction dir;

    public int slide;

    public int changeScale;

    public int changeCur;

    @Override
    public void keyPressed(){
        if (keyCode == LEFT){
            doddleX -= 4;

            if (doddleX < 0){
                doddleX = 399;
            }
        }else if (keyCode == RIGHT){
            doddleX += 4;
            if (doddleX >= 400){
                doddleX = 0;
            }
        }

        if (keyCode == UP){
            slideScreen();
        }
    }

    public void doddleDraw(){
        fill(0, 255, 0);

        // draw the head
        ellipse(doddleX + 11, doddleY - 45, 30, 30);

        // draw the body
        rect(doddleX, doddleY - 30, 22, 30);

        // draw the leg
        rect(doddleX, doddleY, 8, 15);

        rect(doddleX + 14, doddleY, 8, 15);


        if (dir == Direction.UP){

            doddleY -= 2;
            step += 2;
            if (step >= maxStep){
                dir = Direction.DOWN;
                step = 0;
            }

            if (doddleY <= 75){
                dir = Direction.DOWN;
                step = 0;
            }
        }

        if (dir == Direction.DOWN){
            doddleY += 2;
            newPlatform();
        }
    }

    public void newPlatform(){
        if (board[doddleY + 15].getType() != RowType.EMPTY){
            if (doddleX + 22 >= board[doddleY + 15].getPlatformX() && doddleX <= board[doddleY + 15].getPlatformX() + 80){
                dir = Direction.UP;
                if (board[doddleY + 15].getType() == RowType.ONETIME){
                    board[doddleY + 15].type = RowType.EMPTY;
                }
                slideScreenControl();
            }
        }

        if (board[doddleY + 16].getType() != RowType.EMPTY){
            if (doddleX + 22 >= board[doddleY + 16].getPlatformX() && doddleX <= board[doddleY + 16].getPlatformX() + 80){
                dir = Direction.UP;
                if (board[doddleY + 16].getType() == RowType.ONETIME){
                    board[doddleY + 16].type = RowType.EMPTY;
                }
                slideScreenControl();
            }
        }
    }

    public void slideScreenControl(){

        if (slide > 0){
            return;
        }

        if (doddleY <= 450){
            slide = 450 - doddleY;
        }else{
            slide = 0;
        }


    }

    public void settings() {
        size(400, 600); // Set the size of the sketch window
    }

    public void setup(){
        size(400, 600);
        boardSetUp();
        doddleX = 200;
        doddleY = 450;
        dir = Direction.UP;
        slide = 0;
        changeScale = 50;
        changeCur = 0;
    }

    Row[] board = new Row[600];

    public void boardSetUp(){

        Random rand = new Random();
        Random randx = new Random();

        for (int i = 0; i < 600; i++){
            int randomNum = rand.nextInt(100);
            if (randomNum < 95){
                board[i] = new Row(RowType.EMPTY, -1);
            }else{
                if (randomNum < 98){
                    board[i] = new Row(RowType.CONSTANT, randx.nextInt(319) + 40);
                }else if (randomNum < 99){
                    board[i] = new Row(RowType.ONETIME, randx.nextInt(319) + 40);
                }else{
                    board[i] = new Row(RowType.FLOAT, randx.nextInt(319) + 40);
                }

                for (int j = i + 1; j < i + 50 && j < 600; j++){
                    board[j] = new Row(RowType.EMPTY, -1);
                }
                i += 49;
            }
        }

    }

    public void draw() {
        background(255);

        if (slide > 0){
            slideScreen();
            slideScreen();
            slideScreen();
            slideScreen();
            printBoard();
            doddleDraw();
            floatPlatformMove();
            slide--;
            slide--;
            slide--;
            slide--;
            if (dir == Direction.DOWN){
                slide = 0;
            }
            return;
        }

        printBoard();
        floatPlatformMove();
        doddleDraw();
    }

    private void floatPlatformMove() {
        for (int i = 0; i < 600; i++){
            if (board[i].getType() == RowType.FLOAT){
                board[i].floatMove();
            }
        }
        changeCur++;
        if (changeCur == changeScale){
            changeDirection();
            changeCur = 0;
        }
    }

    public void changeDirection(){
        for (int i = 0; i < 600; i++){
            if (board[i].getType() == RowType.FLOAT){
                board[i].changeDir();
            }
        }
    }

    public void printBoard(){
        for (int i = 0; i < 600; i++){
            if (board[i].getType() == RowType.EMPTY){
                continue;
            }

            if (board[i].getType() == RowType.CONSTANT){
                fill(255, 144, 66);
            }else if (board[i].getType() == RowType.ONETIME){
                fill(255, 255, 0);
            }else if (board[i].getType() == RowType.FLOAT){
                fill(224,255,255);
            }
            rect(board[i].getPlatformX(), i, 80, 20);
        }
    }

    public void slideScreen(){
        for (int i = 599 - 1; i >= 1; i--){
            board[i] = board[i - 1];
        }

        if (firstPlatform() < 60){
            board[0] = new Row(RowType.EMPTY, -1);
        }else{
            int rand = ((int) (Math.random() * 100));
            if (rand < 80){
                board[0] = new Row(RowType.CONSTANT, (int) (Math.random() * 400));
            }else if (rand < 95){
                board[0] = new Row(RowType.ONETIME, (int) (Math.random() * 400));
            }else{
                board[0] = new Row(RowType.FLOAT, (int) (Math.random() * 400));
            }

        }

    }

    public int firstPlatform(){
        int res = 0;
        for (int i = 0; i < 600; i++){
            if (board[i].getType() != RowType.EMPTY){
                res = i;
                break;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        PApplet.main("Sketch"); // Replace with the name of your sketch class
    }
}
