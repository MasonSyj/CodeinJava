public class Main {
    public static void main(String[] args){
        Mammal[] mms = new Mammal[10];
/*
        for(Mammal one: mms){
            int x = ((int)( 30 * Math.random()));
            if (x < 10){
                one = new Dog(10, 5);
            }else if (x < 20){
                one = new Dolphin(20, 10);
            }else {
                one = new Lion(15, 20);
            }
        }
*/
        for (int i = 0; i < mms.length; i++){
            int x = ((int)( 30 * Math.random()));
            if (x < 10){
                mms[i] = new Dog(10, 5);
            }else if (x < 20){
                mms[i] = new Dolphin(20, 10);
            }else {
                mms[i] = new Lion(15, 20);
            }
        }

        for(Mammal one: mms){
            one.makeNoise();

        }

        System.out.println("---------------Separate Line-----------------------");

        Pet[] pets = new Pet[20];
        for (int i = 0; i < pets.length; i++) {
            int y = ((int) (30 * Math.random()));
            if (y < 10) {
                pets[i] = new Dog(10, 5);
            } else if (y < 20) {
                pets[i] = new Robot(50, 100);
            } else {
                pets[i] = new Lion(15, 20);
            }
        }
        for(Pet one: pets){
            one.eat();
            if (one instanceof Dog){
                System.out.println("*******************");
                ((Dog) one).sit();
                System.out.println("*******************");
            }
        }
    }
}
