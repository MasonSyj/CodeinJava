package Employee;

public class ManagerTest {
	
	public static void main(String[] args) {
		var boss = new Manager("Doom", 80000, 2000, 7, 8);
		boss.setBonus(5000);
		System.out.println(boss.getSalary());
		
		var staff = new Employee[3];
		
		staff[0] = boss;
		staff[1] = new Employee("Adam", 40000);
		staff[2] = new Employee("BOb", 50000);
		
		
		
		if (staff[1] instanceof Manager)
		{
			boss = (Manager) staff[1];
		}
		
		for (Employee e: staff){
			System.out.println("name= " + e.getName() + ",salary= " + e.getSalary());
		}
	}
}