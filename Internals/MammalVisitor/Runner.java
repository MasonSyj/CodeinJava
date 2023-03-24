public class Runner {
    public static void main(String [] args){
        Mammal mDog = new Dog();
        Mammal mLion = new Lion();
        Mammal mDolphin = new Dolphin();

        Visitor groomingVisitor = new GroomingVisitor();
        Visitor vetVisitor = new VetVisitor();

        mDog.accept(groomingVisitor);
        mDog.accept(vetVisitor);

        mLion.accept(groomingVisitor);
        mLion.accept(vetVisitor);

        mDolphin.accept(groomingVisitor);
        mDolphin.accept(vetVisitor);

    }
}
