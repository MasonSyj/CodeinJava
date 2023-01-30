package lambda;

import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.time.*;

public class LambdaTest {
	
	public static void main(String[] args) {
		var planets = new String[] {"Mercuy", "Venus", "Earth", "Mars", "Juipter", "Saturn", "Uranus", "Neptune"};
		System.out.println(Arrays.toString(planets));
		
		Arrays.sort(planets);
		System.out.println(Arrays.toString(planets));
		
		Arrays.sort(planets, (first, second) -> first.length() - second.length());
		System.out.println(Arrays.toString(planets));
		
		var timer = new Timer(1000, event -> System.out.println("The time is " + new Date()));
		timer.start();
		
		JOptionPane.showMessageDialog(null, "Quit Program?");
		System.exit(0);
		
//		BiFunction<String,String,Integer> comp
//			= (first, second) -> first.length() - second.length();
		
		int day;
		
		
	}
}