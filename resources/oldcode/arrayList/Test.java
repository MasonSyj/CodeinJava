package arrayList;

import java.util.*;

public class Test {
	public static void main(String[] args) {
//		arrayList define
//		ArrayList<Employee> staff = new ArrayList<Employee>();
//		ArrayList<Employee> staff = new ArrayList<>();
		var staff = new ArrayList<Employee>(2); // 100 elements
		
//		arrayList add
		staff.add(new Employee("Adam", 8000, 2000, 1, 1));
		staff.add(new Employee("Bob", 7000));
//		return size of the arrayList
		System.out.println(staff.size());
//		set: only to replace not add
		var charlie = new Employee("Charlie", 7500);
		staff.set(1,charlie);
//		get 
		Employee bob = staff.get(1);
		Employee adam = staff.get(0);
		staff.add(new Employee("dean", 8500));
		System.out.println(bob.toString());
		Employee dean = staff.get(2);
		System.out.println(dean.toString());
//      arrayList and array
//		construct an array with the size from arrayList, and use the method toArray()
		Employee[] a = new Employee[staff.size()];
		staff.toArray(a);

		
	}
}