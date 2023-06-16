
public class Main {
	
	public static void main(String[] args){
		
		var staff = new Employee[3];
		
		staff[0] = new Manager("Doom", 80000, 2000, 7, 8);
		staff[1] = new Employee("Adam", 40000);
		staff[2] = new Employee("BOb", 50000);
		
		for (Employee e: staff){
			if(e instanceof Manager){
				System.out.print(e.getName() + " is a Manager ");
				((Manager) e).setBonus(5000);
			}else{
				System.out.print(e.getName() + " is just an Employee ");
			}

			System.out.println(e.getinfo());
		}
	}
}