public class Lion extends Mammal {
    @Override
    public void makeNoise() {
        System.out.println("Roar roar");
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
