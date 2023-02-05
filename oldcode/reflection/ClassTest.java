package reflection;

import java.lang.Class;
import javax.swing.plaf.synth.*;
import java.util.*;

public class ClassTest {
	public static void main(String[] args) {
		var e = new Employee("adam", 8000);
		var f = new Manager("bob", 12000, 2000);
		Class cl = e.getClass();
		String name = e.getClass().getName();
		System.out.println(e.getClass().getName() + " " + e.getName());
		System.out.println(f.getClass().getName() + " " + f.getName());
		
		var generator = new Random();
		Class clg = generator.getClass();
		String nameg = clg.getName();
		System.out.println(nameg);
		
//		String className = "java.util.Random";
//		Class cln = Class.forName(className);
//		Object obj = cln.getConstructor().newInstance();
		
		
		
	}
}