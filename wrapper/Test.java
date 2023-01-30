package wrapper;

import java.util.*;

public class Test {
	// changeable amount of numbers
	public static double max(double... values){
		double largest = Double.NEGATIVE_INFINITY;
		for (double v: values) if (v > largest) largest = v;
		return largest;
	}
	
	public static void main(String[] args) {
		//arrayList parameter only accpet class, so int isnot acceptable, Integer instead
		var list = new ArrayList<Integer>();
		//autoboxing
		list.add(3);
		list.add(Integer.valueOf(4));
		Integer a = list.get(0);
		//autodeboxing
		int b = list.get(1);
		System.out.println(a.intValue());
		
		Integer c = 1000;
		Integer d = 1000;
		//unlike basic type, two warpper will not satifify == equation
		if (c == d){
			System.out.println("Two int wrapper with same value are true in == equation");
		}
		else{
			System.out.println("Two int wrapper with same value are wrong in == equation");
		}
		
		//can accept null, but throws error when doing calculation
		Integer e = null;
		//System.out.println(e.intValue());
		
		String s = "15";
		int x = Integer.parseInt(s);
		System.out.println(x);
		int y = Integer.parseInt(s, 16);
		System.out.println(y);
		
		Integer x2 = Integer.valueOf("16578");
		Integer y2 = Integer.valueOf("23333", 10);
		System.out.println(x2.intValue());
		System.out.println(y2.intValue());
		
		double m = max(3.1, 40.4, -4, 50.5);
		System.out.println(m);
		
	}
}