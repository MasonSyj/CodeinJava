/**
 * This class Generates prime numbers up to a user specified
 * maximum. The algorithm used is the Sieve of Eratosthenes.
 * <p>
 * Eratosthenes of Cyrene, b. c. 276 BC, Cyrene, Libya --
 * d. c. 194, Alexandria. The first man to calculate the
 * circumference of the Earth. Also known for working on
 * calendars with leap years and ran the library at Alexandria.
 * <p>
 * The algorithm is quite simple. Given an array of integers
 * starting at 2. Cross out all multiples of 2. Find the next
 * uncrossed integer, and cross out all of its multiples.
 * Repeat untilyou have passed the square root of the maximum
 * value.
 *
 * @author Alphonse
 * @version 13 Feb 2002 atp
 */
import java.util.*;
public class GPSelfPractice{
	public static int[] generarr(int value){
		int length = value;
		boolean[] arr = new boolean[length];

		for (int i = 0; i < arr.length; i++){
			arr[i] = true;
		}

		arr[0] = arr[1] = false;

		for (int i = 2; i < Math.sqrt(value); i++){
			if (arr[i] == true){
				for (int j = 2 * i; j < value; j += i){
					arr[j] = false;
				}
			}
		}

		int primecnt = 0;
		for (int i = 0; i < arr.length; i++){
			if (arr[i] == true){
				primecnt++;
			}
		}

		int[] res = new int[primecnt];
		int cnt = 0;
		for (int i = 0; i < arr.length; i++){
			if (arr[i] == true){
				res[cnt++] = i;
			}
		}
		return res;
	}

	public static void main(String[] args){
		int[] res = generarr(20);
		for (int i = 0; i < res.length; i++){
			System.out.print(res[i] + " ");
		}
	}
}