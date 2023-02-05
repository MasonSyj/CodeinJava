

public class Main {
    public static void main(String[] args) {
        Clock c1 = new Clock(13, 25, 33);
        for (int i = 0; i < 1000; i++){
            c1.process();
            c1.display();
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                // this part is executed when an exception (in this example InterruptedException) occurs
            }
        }
    }



}