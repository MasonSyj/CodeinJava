package Employee;

import java.time.*;
import java.util.*;

public class Employee {
	private String name;
	private double salary;
	private LocalDate hireday;
	private static int NextId;
	private int id = assignId();
	
	static{
		var generator = new Random();
		NextId = generator.nextInt(10000);
	}
	
	public int assignId(){
		return NextId++;
	}
	
	
	
	public Employee (String name, double salary, int year, int month, int dayofmonth){
		if (name == null){
			this.name = "unknown";
		}
		else {
			this.name = name;
		}		
		this.salary = salary;
		this.hireday = LocalDate.of(year, month, dayofmonth);

	}
	
	public Employee(String name, double salary){
		this.name = name;
		this.salary = salary;
		this.hireday = LocalDate.now();

	}
	
	public Employee(double salary){
		this("Employee#:" + NextId, salary);
	}
	

	public String getName(){
		return name;
	}
	
	public double getSalary(){
		return salary;
	}
	
	public LocalDate getHireDay(){
		return hireday;
	}
	
	public int getId(){
		return id;
	}
	
	public void raiseMoney(int percent){
		salary = salary * (1 + percent / 100.0);
	}
	
	public static void main(String[] args) {
		
		var adam = new Employee(10000);
		var bob = new Employee(12000);
		var charlie = new Employee(11000);
		System.out.println(adam.getId());
		System.out.println(bob.getId());
		System.out.println(charlie.getId());
	}
}