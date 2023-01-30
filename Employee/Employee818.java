import java.util.*;

public class Employee818 {
	private static int NextId;
	private String name;
	private int id = assignId();
	private double salary;
	
	public int assignId(){
		return NextId++;
	}
	
	public Employee818(String name,double salary){
		this.name = name;
		this.salary = salary;
	}
	
	public Employee818(double salary){
		this( "Employee #" + NextId, salary);
		NextId++;
	}
	
	static{
		var generator = new Random();
		NextId = generator.nextInt(10000);
	}
	
	
	
	
	public static void main(String[] args) {
	
		
	}
	
}