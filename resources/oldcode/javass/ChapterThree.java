
// import java.sql.*; 两个类名冲突了
import java.util.*;
import java.time.*;

public class ChapterThree {
	
	public static void main(String[] args) {
/**	System.out.println("Hello World!");
	System.out.println(new Date());
	Date d1 = new Date();
	String s = d1.toString();
	System.out.println(s);	
*/	
/**	LocalDate.now();
	LocalDate birthday = LocalDate.of(1999, 12, 1);
	int year = birthday.getYear();
	int month = birthday.getMonthValue();
	int day = birthday.getDayOfMonth();
	System.out.println(year + "," + month + "," + day);
	LocalDate newdate = birthday.plusDays(2000);
	int nyear = newdate.getYear();
	int nmonth = newdate.getMonthValue();
	int nday = newdate.getDayOfMonth();
	System.out.println(nyear + "," +nmonth + "," + nday);
*/	
	LocalDate date = LocalDate.now();
	int month = date.getMonthValue();
	int day = date.getDayOfMonth();
	
	date = date.minusDays(day - 1);
	DayOfWeek weekday = date.getDayOfWeek();
	int value = weekday.getValue();
	
	System.out.println("Mon Tue Wed Thu Fri Sat Sun");
	for(int i = 0; i < date.getDayOfMonth(); i ++){
		System.out.print("    ");
	}
	while (date.getMonthValue() == month){
		System.out.printf("%3d", date.getDayOfMonth());
		if (date.getDayOfMonth() == day){
			System.out.print("*");
		}else {
			System.out.print(" ");
		}
		if (date.getDayOfWeek().getValue() == 1){
				System.out.println();
		}
		date = date.plusDays(1);
	}
		
	
	
	}
	
}