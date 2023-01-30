
import java.time.*;
import java.util.*;


public class Employee817 {
	//field includes name, salary hiredate
	//constructor 
	//method getname, getsalary, gethiredate, raisesalary
	private String name;
	private double salary;
	private LocalDate hireday;
	
	
	Employee817 (String name, double salary, int year, int month, int dayofmonth){
		if (name == null){
			this.name = "unknown";
		}
		else {
			this.name = name;
		}
//		name = Objects.requireNonNullElse(name, "unknown");
//		name = Objects.requireNonNull(name, "Name cannot be unknown");
		
		this.salary = salary;
		this.hireday = LocalDate.of(year, month, dayofmonth);
	}
	
//	public static void nameSwap(Employee x, Employee y){
//		String temp;
//		temp = x.name;
//		x.name = y.name;
//		y.name = temp;
//	}
//	
//	public static void employeeSwap(Employee x, Employee y){
//		Employee temp = x;
//		x = y;
//		y = x;
//	}
	
//	public static 
	String getName(){
		return name;
	}
	
	double getSalary(){
		return salary;
	}
	
	LocalDate getHireDay(){
		return hireday;
	}
	
	public void raiseMoney(int percent){
		salary = salary * (1 + percent / 100.0);
	}
	
	public static void main(String[] args) {
		Employee817[] staff = new Employee817[3];
		
		staff[0] = new Employee817("adam", 8000, 2005, 12, 1);
		staff[1] = new Employee817("Bob", 9000, 2006, 8, 17);
		staff[2] = new Employee817("Charlie", 10000, 2004, 10, 10);
//		var Tarry = new Employee("Harry", 8500, 2010, 7, 7);
//		var Tom = new Employee("Tom", 7500, 2008, 8, 8);
//		var NoName = new Employee(null, 7500, 2008, 8, 8);
//		System.out.println(NoName.getName());
		
		
//		System.out.println(staff[0].name);
//		System.out.println(staff[1].name);
//		
//		nameSwap(staff[0], staff[1]);
//		System.out.println(staff[0].name);
//		System.out.println(staff[1].name);
		
//		System.out.println(staff[0].name);
//		System.out.println(staff[1].name);
//		employeeSwap(staff[0], staff[1]);
//		System.out.println(staff[0].name);
//		System.out.println(staff[1].name);
		
		
		
		for (Employee817 e: staff){
			e.raiseMoney(5);
		}
		
		for(Employee817 e: staff){
			System.out.printf("Employee's name: %s, salary: %.2f ", e.name, e.salary);
			System.out.println(e.getHireDay());
		}
		
	}
	

	
	
}