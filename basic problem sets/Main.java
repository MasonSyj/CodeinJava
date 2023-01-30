import java.time.*;
import java.util.*;



public class Main{
	
	public static void main(String[] args) {
//题目：有一分数序列：2/1，3/2，5/3，8/5，13/8，21/13...求出这个数列的前20项之和。
		double a, b;
		double sum = 0;
		a = 2;
		b = 1;
		int step = 0;
		
		while (step < 20){
			System.out.println(a + "/" + b);
			sum += a / b;
			a += b;
			b = a - b;
			step++;
		}
		System.out.println(sum);
}

}
/**
古典问题：有一对兔子，从出生后第3个月起每个月都生一对兔子，
小兔子长到第三个月后每个月又生一对兔子，
假如兔子都不死，问每个月的兔子对数为多少？
*/
/**
		int x = 1;
		int y = 1;
		int n = 0;
		System.out.print(x+", ");
		System.out.print(y+", ");
		while(n<10){
			int z = x + y;
			x = y;
			y = z;
			System.out.print(z+", ");
			n++;
*/	
	
//题目：判断101-200之间有多少个素数，并输出所有素数。
	
//		for(int n = 2; n<=200 ;n++){
//			int i = 2;
//			for(i = 2; i <= Math.sqrt(n);i++){
//				if (n % i == 0){
//					break;
//				}
//			}
//			if (i > Math.sqrt(n)){
//				System.out.println(n + "为素数");
//			}
//		}
	
//题目：打印出所有的"水仙花数"，所谓"水仙花数"是指一个三位数，
//其各位数字立方和等于该数本身。
//例如：153是一个"水仙花数"，因为153=1的三次方＋5的三次方＋3的三次方。
	
//		for(int n = 100;n <= 999; n++){
//			int temp = n;
//			int n1 = temp % 10;
//			temp /= 10;
//			int n2 = temp % 10;
//			temp /= 10;
//			int n3 = temp % 10;
//			if (n == Math.pow(n1, 3) + Math.pow(n2, 3) + Math.pow(n3, 3))
//				System.out.println(n+"为水仙花数");
	
// 将一个正整数分解质因数。例如：输入90,打印出90=2*3*3*5。
	
//public static int decompose(int n){
//	int i;
//	for(i = 2; i <= Math.sqrt(n); i++){
//		if (n % i == 0){
//			return i;
//		}
//	}
//	return n;
//}
//int n = 90;
//while ( n != 1){
//	int x = decompose(n);
//	n = n / x;
//	System.out.println(x+", ");
//}



//题目：输入两个正整数m和n，求其最大公约数和最小公倍数。
//public static int gcd(int m, int n){
//	int x;
//	while (n != 0){
//		x = m % n;
//		m = n;
//		n = x;
//	}
//	return m;
//}
//
//public static int lct(int m, int n){
//	int x = gcd(m, n);
//	return m * n / x;
//}



//题目：求s=a+aa+aaa+aaaa+aa...a的值，其中a是一个数字。
//例如2+22+222+2222+22222(此时共有5个数相加)，几个数相加有键盘控制。	
//public static int add1(int a, int n){
//	int sum = a;
//	for(int i=2;i<=n;i++){
//		a *= 10;
//		sum += a;
//		
//	}
//	return sum;
//}
//		
//public static int add2(int a, int n){
//	int numberEachTime = a;
//	int sum = 0;
//	for(int i=1;i<=n;i++){
//		sum += numberEachTime;
//		numberEachTime = numberEachTime * 10 + a;
//	}
//	return sum;
//}


//题目：一个数如果恰好等于它的因子之和，这个数就称为"完数"。
//例如6=1＋2＋3.编程找出1000以内的所有完数。
//		public static void getFactor(int n){
//			int i;
//			for(i = 1; i < n; i++){
//				if (n % i == 0){
//					System.out.println(i);
//				}
//			}
//		}
//		
//		public static int getFactorSum(int n){
//			int i;
//			int sum = 0;
//			for(i = 1; i < n; i++){
//				if (n % i == 0){
//					sum += i;
//				}
//			}
//			return sum;
//		}		
//	int n = 28;
//	int sum = getFactorSum(n);
//	if (n == sum){
//		System.out.println("Complete Number");
//	}
//		
//	for(int i=1; i<10000; i++){
//		if (i == getFactorSum(i))
//			System.out.println("Complete Number:" + i);
//	}	

//题目：一球从100米高度自由落下，每次落地后反跳回原高度的一半；再落下，
//求它在第10次落地时，共经过多少米？第10次反弹多高？
//	int begin = 100;
//	double eachDistance = begin;
//	int n = 20;
//	double[] fall = new double[n/2+1];
//	double[] rise = new double[n/2+1];
//	double[] move = new double[n/2];
//	double sum = 0;
//	int i = 0;
//	while(i < n/2){
//		sum += eachDistance * 1.5;
//		fall[i] = eachDistance;
//		eachDistance /= 2;
//		rise[i] = eachDistance;
//		i++;
//		
//	}
//	
//	for(double value: fall){
//		System.out.println(value);
//	}
//	
//	System.out.println("-------------------------");
//		
//	for(double value: rise){
//		System.out.println(value);
//	}
//		
//	System.out.println(sum);


//	输入某年某月某日，判断这一天是这一年的第几天？	
//	Scanner in = new Scanner(System.in);
//	int year, month, dayOfMonth;
//	System.out.println("请输入要一个日期,依次输入年月日，以逗号分隔");
//	year = in.nextInt();
//	month = in.nextInt();
//	dayOfMonth = in.nextInt();
//	LocalDate date = LocalDate.of(year, month, dayOfMonth);
//	
//	int day = date.getDayOfYear();
//	System.out.println(day);	

//输入三个整数x,y,z，请把这三个数由小到大输出。
//	Scanner in = new Scanner(System.in);
//	int x, y, z;
//	int temp;
//	x = in.nextInt();
//	y = in.nextInt();
//	z = in.nextInt();
//	
//	if (x > y){
//		temp = x;
//		x = y;
//		y = temp;
//	}
//	
//	if (z < y){
//		if (z < x){
//			temp = y;
//			y = x;
//			x = z;
//			z = temp;
//		}
//		else {
//			temp = y;
//			y = z;
//			z = temp;
//		}
//	}


//题目：输出9*9口诀。
//	for (int x = 1; x < 10; x++){
//		for (int y = 1; y <= x; y++){
//			System.out.print(x + "*" + y +  "=" + x * y + "  ");
//		}
//		System.out.println();
//	}


/**	题目：猴子吃桃问题：
*	猴子第一天摘下若干个桃子，当即吃了一半，还不瘾，又多吃了一个 
*	第二天早上又将剩下的桃子吃掉一半，又多吃了一个。
*	以后每天早上都吃了前一天剩下的一半零一个。
*	到第10天早上想再吃时，见只剩下一个桃子了。求第一天共摘了多少。
*/
//		int count = 0;
//		int i;
//		int x;
//		for(i = 1; i < 65536; i++){
//			x = i;
//			count = 0;
//			while (count != 10 ){
//				x = eatOneDay(x);
//				count++;
//				if (x == 0)
//				break;
//			}
//			if(x == 1)
//			System.out.println(i);
//		}
//		
//		
//	int m = 1;
//	for(int z = 0; z < 10; z++){
//		m = 2 * (m + 1);
//	}
//	
//	System.out.println(m);
//public static int eatOneDay(int x){
//	return x - (x / 2 + 1);
//}


//打印菱形
//		char[][] shape = new char[7][7];
//		int middle = 7 / 2;
//		int i, j;
//		for (i = 0; i < 4; i++){
//			for (j = 0; j < 7; j++){
//				if (j <= middle + i && j >= middle - i )
//					shape[i][j] = '*';
//				else 
//					shape[i][j] = ' ';
//			}
//		}
//		
//		for(int z = 4; z < 7; z++){
//			for (j = 0; j < 7; j++){
//				shape[z][j] = shape[6-z][j];
//			}
//		}
//		
//		for (i = 0; i < 7; i++){
//			for (j = 0; j < 7; j++){
//			System.out.print(shape[i][j]);
//			}
//			System.out.println();
//		}
