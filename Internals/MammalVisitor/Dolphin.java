public class Dolphin extends Mammal {
    @Override
    public void makeNoise() {
        System.out.println("Squeak click");
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
