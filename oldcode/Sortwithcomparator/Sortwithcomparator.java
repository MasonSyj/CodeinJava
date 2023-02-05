package lambda;

import java.util.*;

public class Sortwithcomparator {
	
	public static void main(String[] args) {
		var planets = new String[] {"Mercuy", "Venus", "Earth", "Mars", "Juipter", "Saturn", "Uranus", "Neptune"};
		System.out.println(Arrays.toString(planets));
		
		Arrays.sort(planets);
		System.out.println(Arrays.toString(planets));

		//comparator
		Arrays.sort(planets, (first, second) -> first.length() - second.length());
		System.out.println(Arrays.toString(planets));

	}
}