public class Runner {
    public static void main (String [] args){
        Mammal mDolphin = new Dolphin();
        Mammal mDog = new Dog();
        Mammal mLion = new Lion();
        Dog realDog = new Dog();

        //Just single dispatch
        mDolphin.makeNoise();
        mDog.makeNoise();
        mLion.makeNoise();

        mLion.acceptMammal(mDog);

        mLion.acceptMammal(realDog);


//        mDog.acceptMammal(mLion);
    }
}
