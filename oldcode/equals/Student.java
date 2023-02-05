package equals;

public class Student extends Person {
	
	private String major;
	
	public Student(String name, String major){
		super(name);
		this.major = major;
	}
	
	public String getDescription(){
		return String.format("a student major in %s", major);
	}
	
	
}