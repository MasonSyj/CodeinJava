import java.util.Scanner;

import java.util.*;

public class CharCount{

	public static void main(String[] args){
		System.out.print("请输入一串字符：");
		Scanner scan = new Scanner(System.in);
		String str = scan.nextLine();//将一行字符转化为字符串
		scan.close();
		count(str);

}

//统计输入的字符数

private static void count(String str){

	String E1 = "[\u4e00-\u9fa5]";//汉字
	String E2 = "[a-zA-Z]";
	String E3 = "[0-9]";
	String E4 = "\\s";//空格
	
	int countChinese = 0;
	int countLetter = 0;
	int countNumber = 0;
	int countSpace = 0;
	int countOther = 0;

	char[] array_Char = str.toCharArray();//将字符串转化为字符数组
	String[] array_String = new String[array_Char.length];//汉字只能作为字符串处理

	for(int i=0;i<array_Char.length;i++)
	array_String[i] = String.valueOf(array_Char[i]);

//遍历字符串数组中的元素

	for(String s:array_String){
		if(s.matches(E1))
		countChinese++;
		else if(s.matches(E2))
		countLetter++;
		else if(s.matches(E3))
		countNumber++;
		else if(s.matches(E4))
		countSpace++;
		else 
		countOther++;
	}

	System.out.println("输入的汉字个数："+countChinese);
	System.out.println("输入的字母个数："+countLetter);
	System.out.println("输入的数字个数："+countNumber);
	System.out.println("输入的空格个数："+countSpace);
	System.out.println("输入的其它字符个数："+countSpace);

}

}



class Prog7_2{
	public static void main(String[] args){

	System.out.println("请输入一行字符：");
	Scanner scan = new Scanner(System.in);
	String str = scan.nextLine();
	scan.close();
	count(str);

}

//统计输入的字符

private static void count(String str){

	List<String> list = new ArrayList<String>();
	char[] array_Char = str.toCharArray();

	for(char c:array_Char)
	list.add(String.valueOf(c));//将字符作为字符串添加到list表中

	Collections.sort(list);//排序

	for(String s:list){
	int begin = list.indexOf(s);
	int end = list.lastIndexOf(s);

//索引结束统计字符数

	if(list.get(end)==s)
	System.out.println("字符‘"+s+"’有"+(end-begin+1)+"个");

}

}

}

