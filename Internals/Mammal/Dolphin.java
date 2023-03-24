public class Dolphin extends Mammal{
    @Override
    public void makeNoise() {
        System.out.println("squeek click");
    }
    public void acceptMammal(Mammal m) {
       m.makeNoise(this);
    }
    @Override
    public void makeNoise(Dog d) {
        System.out.println("Dolphin interacting with dog");
    }
    @Override
    public void makeNoise(Dolphin d) {
        System.out.println("Dolphin interacting with dolphin");
    }

    @Override
    public void acceptMammal(Dog dog) {

    }

    @Override
    public void makeNoise(Lion l) {
        System.out.println("Dolphin interacting with lion");
    }
}
