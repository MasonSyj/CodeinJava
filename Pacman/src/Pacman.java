    public class Pacman extends Cell implements Draw{
    int eatFigures;

    public Pacman(){
        super(true);
        this.eatFigures = 0;
    }

    public void eat(){
        this.eatFigures++;
    }

        @Override
        public void toDefaultString() {
            super.toDefaultString();
            System.out.println("Pacman ");
        }

        @Override
    public void draw() {
        System.out.print("X");
    }
}
