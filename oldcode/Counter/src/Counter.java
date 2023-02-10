

public class Counter {

    private int num;
    private int price;

    public int getNum(){
        return this.num;
    }

    public int getPrice(){
        return price;
    }

    public Counter(int num, int price){
        this.num = num;
        this.price = price;
    }
    public static void main(String[] args) {
        Counter apple = new Counter(3, 5);
        Counter orinage = new Counter(2, 6);
        Counter pear = new Counter(4, 3);
        System.out.println("Apple's num and price: " + apple.getNum() + "& " + apple.getPrice());
        System.out.println("orinage's num and price: " + orinage.getNum() + "& " + orinage.getPrice());
        System.out.println("pear's num and price: " + pear.getNum() + "& " + pear.getPrice());

        String str1 = new String("Hello World");
        char c1 = str1.charAt(6);
        boolean b1 = str1.contains("app");
        int len = str1.length();
        String lowstr = str1.toLowerCase();
        String substr = str1.substring(1, 5);
        int substrlen = substr.length();
        System.out.printf("c1: %c, b1: %b, len: %d, lowerstr: %s, substring: %s", c1, b1, len, lowstr, substr);
        System.out.println(substrlen);
    }
}