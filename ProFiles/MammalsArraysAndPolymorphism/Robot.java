public class Robot implements Pet{
    private int age;
    private int powerlevel;

    public Robot(int age, int powerlevel){
        this.age = age;
        this.powerlevel = powerlevel;
    }


    @Override
    public void eat() {
        System.out.println("Robot dones't eat but charge");
        this.age--;
    }
}
