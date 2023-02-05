import java.util.*;

public class CommonClass {
	
	public static void StringSort(String[] a){
		for(int i = 0; i < a.length; i++){
			for (int j = 0; j < a.length - i - 1; j++){
				if (a[j].compareTo(a[j+1]) < 0){
					String temp;
					temp = a[j];
					a[j] = a[j+1];
					a[j+1] = temp;
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		
//		String s1 = new String("Hello World!");
//		char a[] = {'J', 'a', 'v', 'a',' ', 'L'};
//		String s2 = new String(a);
//		String s3 = new String(a,2,4);  //2 means starting index , 4 means total 4 chars
//		
//		int n1 = s1.length();
//		int n2 = s2.length();
//		System.out.println(n1 + " " + n2);
//		
//		boolean b1 = s1.equals(s2);
//		System.out.println(b1);
//		
//		boolean b2 = s1.startsWith("Hello");
//		System.out.println(b2);
//		
//		boolean b3 = s1.endsWith("World");
//		System.out.println(b3);
//		
//		int n3 = s1.compareTo(s2);
//		System.out.println(n3);
//		
//		String[] sa1 = {"Melon", "Apple", "Pear", "Banana"};
//		StringSort(sa1);
//		for(String s : sa1){
//			System.out.println(s);
//		}
//		
//		boolean b4 = s1.contains("z");
//		System.out.println(b4);
//		
//		String tom = "I am a good cat";
//		System.out.println(tom.indexOf("a"));
//		System.out.println(tom.indexOf("good"));
//		System.out.println(tom.indexOf("a",7));
//		System.out.println(tom.indexOf("w",2));
//		
//		String subTom = tom.substring(5, 11);
//		System.out.println(subTom);
		
///////////////////////////////////////////////////////////
		
//		int x;
//		String value1 = "876";
//		x = Integer.parseInt(value1);
//		System.out.println(x);
//		
//		String value2 = String.valueOf(3.1415926);
//		String value3 = String.valueOf(1911);
//		System.out.println(value2);
//		System.out.println(value3);
		
//////////////////////////////////////////////////////////
		
//		String s1 = "Common Class Practice";
//		char[] a = new char[4];
//		char[] b;
//		s1.getChars(13, 17, a, 0);
//		System.out.println(a);
//		b = "Sic Parvis Mangna".toCharArray();
//		for(char c : b){
//			System.out.print(c);
//		}
//		
//		String s2 = "Sic Parvis Mangna";
//		String regex = "[\\s\\d\\p{Punct}]+";
//		String[] word = s2.split(regex);
//		for(String s : word){
//			System.out.print(s+" ");
//		}

////////////////////////////////////////////////////////
//StringTokenizer
		String s = "you are welcome(thank you), nice to meet you";
		StringTokenizer stringAnalysis = new StringTokenizer(s,"(), ");
		int number = stringAnalysis.countTokens();
		System.out.println("number of word included: " + number);
		while(stringAnalysis.hasMoreTokens()){
			String subString = stringAnalysis.nextToken();
			int num = 0;
			System.out.println("Word" + num + ":" + subString);
			num++;
		}
		
////////////////////////////////////////////////////////
		
	}
}