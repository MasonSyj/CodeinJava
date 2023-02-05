package equals;

import java.time.*;
import javax.lang.model.element.*;

public class Employee { // extends Person {
	private String name;
	private double salary;
	private LocalDate hireDay;
	
	public Employee (String name, double salary, int year, int month, int dayOfMonth){
//		super(name);
		this.name = name;
		this.salary = salary;
		hireDay = LocalDate.of(year, month, dayOfMonth);
	}
	
	public LocalDate getHireDay(){
		return hireDay;
	}
	
	public void raiseSalary(int rate){
		salary = salary + salary * rate / 100.0;
	}
	
	public String getDescription(){
		return String.format("an employee with a salary of %.2f", salary);
	}
	
	@Override
	public boolean equals(Object otherObject){
		// a quick test to see if equal
		if (this == otherObject) return true;
		// if otherObject is null, false naturally
		if (otherObject == null) return false;
		// if two things are different in class, false naturally
		if (this.getClass() != otherObject.getClass()) return false;
		
		if(!(otherObject instanceof Employee)) return false;
		
		Employee other = (Employee) otherObject;
		
		return name.equals(other.name) && salary == other.salary && hireDay.equals(other.hireDay);
	}
	
	public int hashCode(){
		return 7 * name.hashCode() + 11 * Double.hashCode(salary) + 13 * hireDay.hashCode();
	}
	
	public String toString(){
//		return "Employee[name=" + name + ",salary=" + salary + ",hireDay=" + hireDay + "]";
		return getClass().getName() + "[name=" + name + ",salary=" + salary + ",hireDay=" + hireDay + "]";
	}
	
	public static void main(String[] args){
		var adam = new Employee("adam", 8000, 2000, 1, 1);
		System.out.println(adam.toString());
	}
}