package StaticChecking;

import java.util.*;


public class StaticChecking {
	
	public static List<Integer> hailstoneSequnce(int n){
		List<Integer> list = new ArrayList<Integer>();
		while (n != 1){
			list.add(n);
			if ( n % 2 == 0)
				n /= 2;
			else 
				n = 3 * n + 1;
		}
		list.add(1);
		return list;
	}
	
	
	public static void main(String[] args) {
		int[] array = new int[10];
		int c = array.length; // an instance variable rather than a method
		System.out.println(c);
		
		var b = new ArrayList<Integer>();
		b.add(10);
		b.add(12);
		
		List<Integer> list4 = hailstoneSequnce(4);
		var list5 = hailstoneSequnce(5);
		for (int x: list4){
			System.out.print(x + " ");
		}
		
		System.out.println();
		
		for (int x: list5){
			System.out.print(x + " ");
		}
	}
}