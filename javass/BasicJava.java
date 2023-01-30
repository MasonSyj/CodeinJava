public class BasicJava {
	public static void f(String s, StringBuilder sb){
		s.concat("b");
		s += "c";
		sb.append("d");
	}
	
	public static void main(String[] args) {
		
	String t = "a";
	String newt = new String("a");
	t += "c";
	StringBuilder tb = new StringBuilder(t);
	f(t,tb);
	System.out.println(t);
	System.out.println(tb);
	f(newt,tb);
	System.out.println(newt);
	}
	
	
	
}