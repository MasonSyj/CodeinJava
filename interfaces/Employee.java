package interfaces;

import javax.xml.crypto.dsig.keyinfo.*;

public class Employee implements Comparable<Employee>{
	
	private String name;
	private double salary;
	private static int employeeId = 1;
	private int rank;
	
	public Employee(String name, double salary){
		this.name = name;
		this.salary = salary;
		this.rank = assignEmployeeID();
	}
	
	public int assignEmployeeID(){
		return employeeId;
	}
	
	public String getName(){
		return name;
	}
	
	public double getSalary(){
		return salary;
	}
	
	public void raiseSalary(int rate){
		salary = salary + salary * rate / 100;
	}
	
	public int getRank(){
		return rank;
	}
	
	public final int compareTo(Employee other){
//		if(getClass() != other.getClass()) throw new ClassCastException();
		
		if (rank != other.rank)  return Integer.compare(other.rank, rank);
		
		else return Double.compare(other.salary, salary);
	}
	
	
}