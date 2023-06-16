package equals;

public class HashCode {
	public static void main(String[] args) {
		var s = "OK";
		var sb = new StringBuilder(s);
		System.out.println(s.hashCode() + " " + sb.hashCode());
		var t = new String("OK");
		var tb = new StringBuilder(t);
		System.out.println(t.hashCode() + " " + tb.hashCode());
	}
}