package Cellss;

import java.util.*;

public class Field {
	private int height;
	private int width;
	private Cell[][] field;
	
	public Field(int width, int height){
		this.width = width;
		this.height = height;
		field = new Cell[height][width];
	}
	
	public int getHeight(){return height;}
	
	public int getWidth(){return width;} 
	
	public void place(int row, int col, Cell o){
//		Cell ret = field[row][col];
		field[row][col] = o;
//		return ret;
	}
	
	public Cell get(int row, int col){
		return field[row][col];
	}
	
	public Cell[] getNeighbour(int row, int col){
		var list = new ArrayList<Cell>();
		for(int i = -1; i<2; i++){
			for(int j = -1; j<2;j++){
				int r = row + i;
				int c = col + j;
				if (r>-1 && r<height && c>-1 && c<width && !(r == row && c == col)){
					list.add(field[r][c]);
				}
			}
		}
		return list.toArray(new Cell[list.size()]);
	}
	
}