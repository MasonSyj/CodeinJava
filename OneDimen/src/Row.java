public class Row {
    RowType type;

    int platformX; //  0 <= x < 100

    Direction dir;
    // the length is 80,
    public Row(RowType type, int platformX) {
        this.type = type;
        this.platformX = platformX;
        if (type == RowType.FLOAT){
            if (Math.random() < 0.5){
                dir = Direction.LEFT;
            }else{
                dir = Direction.RIGHT;
            }
        }
    }

    public void changeDir(){
        if (dir == Direction.LEFT){
            dir = Direction.RIGHT;
        }else{
            dir = Direction.LEFT;
        }
    }

    public void floatMove(){
        if (dir == Direction.LEFT){
            platformX--;
        }else{
            platformX++;
        }
    }

    public RowType getType(){
        return this.type;
    }

    public int getPlatformX(){
        return this.platformX;
    }

}
