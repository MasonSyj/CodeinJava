package ChapterOneProblemSet;

import java.util.*;

public class OrderedCollection<Object> implements Comparable<Object>{
	
	private Set<Object> set= new HashSet<>();
	
	public void makeEmpty(){
		set.clear();
	}
	
	public boolean isEmpty(){
		return set.equals(new HashSet<>());
	}
	
	public void insert(Object e){
		set.add(e);
	}
	
	public void remove(Object e){
		set.remove(e);
	}
	
	public boolean isPresent(Object e){
		return set.contains(e);
	}
	
	@Override
	public int compareTo(Object other){
		return 
	}
	public Object findMin(){
		
	}
	
	public Object findMax(){
		
	}
	
	public static void main(String[] args) {
		
	}
}