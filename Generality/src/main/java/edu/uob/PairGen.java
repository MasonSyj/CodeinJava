package edu.uob;

public class PairGen<K, Q, J> {
    private K k;
    private Q q;
    private J j;

    public PairGen(K king, Q queen, J jane){
        this.k = king;
        this.q = queen;
        this.j = jane;
    }

    public void setJ(J j) {
        this.j = j;
    }

    public void setQ(Q q) {
        this.q = q;
    }

    public void setK(K k) {
        this.k = k;
    }

    public J getJ() {
        return j;
    }

    public K getK() {
        return k;
    }

    public Q getQ() {
        return q;
    }

    public static void main(String[] main){
        PairGen<Integer, Double, String> paria = new PairGen<Integer, Double, String>(3, 4.5, "hello");
        System.out.println(paria.getK() + " " + paria.getQ() + " " +  paria.getJ());
    }
}
