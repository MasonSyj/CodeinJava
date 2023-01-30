package javass;

import javax.swing.plaf.synth.*;

public class InterestRate38 {
	public static void main(String[] args) {
		int row = 10;
		int col = 6;
		double [][]balances = new double[row][col];
		double []interest = new double[col];
		
		for (int i = 0;i<interest.length;i++)
			interest[i] = (i+10) * 0.01;
		
		for (int i = 0;i<balances[0].length;i++)
			balances[0][i] = 100000.00;
			
		
		for (int i = 1;i<balances.length;i++)
			for(int j = 0;j<balances[i].length;j++){
				balances[i][j] = balances[i-1][j] * (1 + interest[j]);
			}
		
		for (double[] hang: balances){
			for (double item: hang){
				System.out.printf("%10.2f  ", item);
			}
			System.out.println();
		}
	}
}