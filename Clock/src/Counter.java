public class Counter {
    private int limit;
    private int cur;

    public Counter(int limit, int cur){
        this.limit = limit;
        this.cur = cur;
    }

    public void tick(){
        cur++;
        if (cur == limit){
            cur = 0;
        }
    }

    public int getCur(){
        return cur;
    }
}
