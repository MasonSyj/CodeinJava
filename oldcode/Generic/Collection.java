package ChapterOneProblemSet;

import java.util.*;

public class Collection<T>{

	private Set<T> set= new HashSet<T>();
	
	public void makeEmpty(){
		set.clear();
	}
	
	public boolean isEmpty(){
		return set.equals(new HashSet<>());
	}
	
	public void insert(T e){
		set.add(e);
	}
	
	public void remove(T e){
		set.remove(e);
	}
	
	public boolean isPresent(T e){
		return set.contains(e);
	}

	public boolean equal(T other){
		return set.equals(other);
	}

/*
	public T findMin(){
		return
	}
	
	public T findMax(){
		
	}
*/


}