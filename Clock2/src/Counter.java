public class Counter {
    private int limit;
    private int cur;

    private Counter next;

    public Counter(int limit, int cur, Counter next){
        this.limit = limit;
        this.cur = cur;
        this.next = next;
    }

    public void tick(){
        cur++;
        if (cur == limit) {
            cur = 0;
            if (next != null) {
                next.tick();
            }
        }
    }

    public int getCur(){
        return cur;
    }
}
