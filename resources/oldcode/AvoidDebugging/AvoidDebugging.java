package AvoidDebugging;

public class AvoidDebugging {
	
//	public void testAssertionsEnabled(){
//		assertThrows(AssertionError.class, () -> {assert false;});
//	}
	
	public static double sqrt(double x){
		assert x >= 0;
		double r;
		r = Math.sqrt(x);
		assert Math.abs(r*r - x) > 1.0;
		return r;
	}
	
	
	public static void main(String[] args) {
		char test0 = 'a';
		final char test1 = test0;
		
		String test2 = test1 + "eiou";
		final String test3 = test2;
		
		char[] test4 = new char[] {test0, 'e', 'i', 'o', 'u'};
		final char[] test5 = test4;
		
		int x = 4;
		System.out.println(sqrt(x));
		
		
		
		
		
	}
}