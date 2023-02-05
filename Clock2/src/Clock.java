public class Clock {
    private Counter sec;
    private Counter min;
    private Counter hour;

    public Clock(int h, int m, int s){
        this.hour = new Counter(24, h, null);
        this.min = new Counter(60, m, hour);
        this.sec = new Counter(60, s, min);
    }

    public void display(){
        System.out.println(hour.getCur() + " : " + min.getCur() + " : " + sec.getCur());
    }

    public void process(){
        sec.tick();
    }
}
