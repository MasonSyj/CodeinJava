package ChapterOneProblemSet;

import java.util.*;

public class Collection<Object> {
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
	
	
	public static void main(String[] args) {
		Collection<Integer> intSet = new Collection<>();
		System.out.println(intSet.isEmpty());
		Integer int1 = 123;
		intSet.insert(123);
		System.out.println(intSet.isPresent(int1));
		
	}
}