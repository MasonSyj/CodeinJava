public class Cell implements Draw{
    boolean isexist;

    public Cell(){
        this.isexist = false;
    }

    public Cell(boolean isexist){
        this.isexist = isexist;
    }

    @Override
    public void draw() {
        System.out.print("_");
    }

    public void toDefaultString(){
        if (this.isexist == true){
            System.out.print("exist");
        }else{
            System.out.println("dead");
        }

    }
}
