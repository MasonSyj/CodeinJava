package comparator;

import java.util.*;

public class LengthComparator implements Comparator<String>{
	
	String first;
	
	public int compare(String first, String second){
		return second.length() - first.length();
	}
	
	public static void main(String[] args) {
		String[] a = new String[3];
		a[0] = "Apple";
		a[1] = "Banana";
		a[2] = "Waterlemon";
		
		Arrays.sort(a, new LengthComparator());
		Arrays.sort(a);
		
		for(String s : a){
			System.out.println(s);
		}
		
	}
}