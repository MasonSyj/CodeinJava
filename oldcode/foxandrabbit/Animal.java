package foxandrabbit;

import javax.xml.stream.*;
import java.util.*;

public abstract class Animal {
	
	private boolean alive = false;
	private int age = 0;
	private int ageLimit;
	private int breedAge;
	
	public Animal(int ageLimit, int breedAge){
		this.ageLimit = ageLimit;
		this.breedAge = breedAge;
	}
	
	public void die(){ alive = false;}
	
	public void reborn() { alive = true;}
	
	public boolean isAlive(){ return this.alive;}
	
	protected int getAge(){
		return age;
	}
	
	protected double getAgePercent(){
		return (double) age/ageLimit;
	}
	
	public void grow(){
		age++;
		if (age > ageLimit){
			die();
		}
	}
	
	public Location move(Location [] freeAdj){
		Location ret = null;
		if (freeAdj.length > 0 && Math.random() < 0.2){
			ret = freeAdj[(int)(Math.random()*freeAdj.length)];
		}
		return ret;
	}
	
	public Animal feed(ArrayList<Animal> neighbour){
		return null;
	}
	
	public abstract Animal breed();
	
	@Override
	public String toString(){
		return ""+age+":"+(isAlive()?"live":"dead");
	}
	
	
	
}