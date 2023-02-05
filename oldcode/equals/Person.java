package equals;

public abstract class Person {
	public abstract String getDescription();
	private String name;
	
	public Person(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	
	public static void main(String[] args) {
		
	}
}