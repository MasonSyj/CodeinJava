import java.util.*;


public class ConstructorTest {
	
	public static void main(String[] args) {
		var staff = new Employee[3];
		staff[0] = new Employee("Harry", 40000);
		staff[1] = new Employee(60000);
		staff[2] = new Employee();
		
		for (Employee e : staff){
			System.out.println("name=" + e.getNmae() + ",id=" + e.getId() + ",salary=" + e.getSalary());
		}
		
	}	
}
class Employee{
	private static int NextId;
	
	private int id;
	private String name = "";
	private double salary;
	
	static{
		var generator = new Random();
		NextId = generator.nextInt(10000);
	}
	
	{
		id = NextId;
		NextId++;
	}
	
	public Employee(String n, double s){
		name = n;
		salary = s;
	}
	
	public Employee(double s){
		this("Employee #" + NextId, s);
	}
	
	public Employee(){
	}
	
	public String getNmae(){
		return name;
	}
	
	public double getSalary(){
		return salary;
	}
	
	public int getId(){
		return id;
	}
}