

public class Manager extends Employee{
	private double bonus;
	
	public Manager(String name, double salary, int year, int month, int dayofmonth){
		super(name, salary, year, month, dayofmonth);
		bonus = 0;
	}
	
	public double getSalary(){
		double baseSalary = super.getSalary();
		return baseSalary + bonus;
	}
	
	public void setBonus(double b){
		this.bonus = b;
	}

	public double getBonus(){
		return this.bonus;
	}
	@Override
	public String getinfo(){
		return super.getinfo() + ", Bonus:" + this.getBonus();
	}
	
	

}