public abstract class Mammal {

    public void stateAttributes(){
        System.out.println("Warm blood, 3 inner ear bones and fur / hair");
    }
    public abstract void makeNoise();
    public abstract void acceptMammal(Mammal m);
    public abstract void makeNoise(Dog d);
    public abstract void makeNoise(Lion l);
    public abstract void makeNoise(Dolphin d);

    public abstract void acceptMammal(Dog dog);


}
