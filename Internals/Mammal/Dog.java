public class Dog extends Mammal {
    @Override
    public void makeNoise() {
        System.out.println("woof");
    }
    public void acceptMammal(Mammal m){
        m.makeNoise(this);
    }
    @Override
    public void makeNoise(Dog d) {
        System.out.println("Dog interacting with dog");
    }
    @Override
    public void makeNoise(Dolphin d) {
        System.out.println("Dog interacting with dolphin");
    }

    @Override
    public void acceptMammal(Dog dog) {

    }

    @Override
    public void makeNoise(Lion l) {
        System.out.println("Dog interacting with lion");
    }
}
