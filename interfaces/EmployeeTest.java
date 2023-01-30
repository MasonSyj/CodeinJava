package interfaces;

import java.util.*;

public class EmployeeTest {
	public static void main(String[] args) {
		var staff = new Employee[3];
		
		staff[0] = new Employee("Adam", 8000);
		staff[1] = new Employee("Bob", 8500);
		staff[2] = new Manager("Charlie", 10000);
		
		Arrays.sort(staff);
		
		for(Employee e : staff){
			System.out.println("name=" + e.getName() + ",salary=" + e.getSalary() + " rank=" + e.getRank());
		}
		
		int x = staff[0].compareTo(staff[1]);
		System.out.println(x);
	}
}