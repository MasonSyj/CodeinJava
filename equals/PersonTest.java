package Person;

public class PersonTest {
	public static void main(String[] args) {
	var adam = new Employee("Adam", 40000, 2000, 1, 1);
	System.out.println(adam.getDescription());
	var bob = new Student("Bob", "Computer Science");
	System.out.println(bob.getDescription());
	}
}