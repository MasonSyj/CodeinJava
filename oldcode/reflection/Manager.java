package reflection;

public class Manager extends Employee{
	private double bonus;
	
	public Manager(String name, double salary, int year, int month, int dayofmonth){
		super(name, salary, year, month, dayofmonth);
		bonus = 0;
	}
	
	public Manager(String name, double salary, double bonus){
		super(name,salary);
		this.bonus = bonus;
	}
	
	public double getSalary(){
		double baseSalary = super.getSalary();
		return baseSalary + bonus;
	}
	
	public void setBonus(double b){
		this.bonus = b;
	}
	
	
	public static void main(String[] args) {
		
	}
}