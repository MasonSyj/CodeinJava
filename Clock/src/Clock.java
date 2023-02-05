public class Clock {
    private Counter sec;
    private Counter min;
    private Counter hour;

    public Clock(int h, int m, int s){
        this.hour = new Counter(24, h);
        this.min = new Counter(60, m);
        this.sec = new Counter(60, s);
    }

    public void display(){
        System.out.println(hour.getCur() + " : " + min.getCur() + " : " + sec.getCur());
    }

    public void process(){
        sec.tick();
        if (sec.getCur() == 0){
            min.tick();
        }

        if (min.getCur() == 0){
            hour.tick();
        }
    }
}
