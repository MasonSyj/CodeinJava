public class Lion extends Mammal {
    @Override
    public void makeNoise() {
        System.out.println("roar");
    }
    public void acceptMammal(Mammal m){
        m.makeNoise(this);
    }

    public void acceptMammal(Dog dog){
        System.out.println("dog don't bark");
    }
    @Override
    public void makeNoise(Dog d) {
        System.out.println("Lion interacting with dog");
    }
    @Override
    public void makeNoise(Dolphin d) {
        System.out.println("Lion interacting with dolphin");
    }
    @Override
    public void makeNoise(Lion l) {
        System.out.println("Lion interacting with lion");
    }
}
