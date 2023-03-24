public class Dog extends Mammal {
    @Override
    public void makeNoise() {
        System.out.println("Woof woof");
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
