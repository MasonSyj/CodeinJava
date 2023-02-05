package interfaces;

public class Manager extends Employee {
	private static int managerId = 2;
	
	public Manager(String name, double salary){
		super(name, salary);
		this.rank = assignManagerId();
	}
	
	
	public int assignManagerId(){
		return managerId;
	}
	
}