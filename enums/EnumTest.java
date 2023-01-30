package enums;

import java.util.*;

//	public enum Size{SMALL, MEDIUM, LARGE, EXTRA_LARGE};
	
public class EnumTest{
	
	public static void main(String[] args) {
		var in = new Scanner(System.in);
		System.out.println("Enter a size:");
		String input = in.next().toUpperCase();
		Size size = Enum.valueOf(Size.class, input);
		System.out.println("Size = " + size);
		System.out.println("abbreviation = " + size.getAbbreviation() );
		if(size == Size.EXTRA_LARGE)
			System.out.println("Good Job");
	}
}

enum Size{
	SMALL("S"), MEDIUM("M"), LARGE("L"), EXTRA_LARGE("XL");
	
	private String abbreviation;
	
	private Size(String abbreviation){
		this.abbreviation = abbreviation;
	}
	public String getAbbreviation(){
		return abbreviation;
	}
}