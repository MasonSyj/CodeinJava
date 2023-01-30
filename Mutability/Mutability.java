package Mutability;

import java.sql.*;

public class Mutability {
	
	public static Date startOfSpring(){
		return askGroundhog();
	}
	
	public static void main(String[] args) {
	String s = "";
	int n = 10;
	for(int i = 0; i < n; i++){
		s = s + i;
		System.out.println(i + " " + s);
	}
	System.out.println("***************");
	System.out.println(s);
	}
	
	
}