public class ArrayInsert {
	private int size = 0;
	private int[] array;
	
	public ArrayInsert(int capacity){
		this.array = new int[capacity];
	}
	
	public void add(int value){
		array[size] = value;
		size++;
	}
	
	public void insert(int index, int value){
		if (index - 1 < 0 || index - 1 > size ){
			System.out.println("Out of array boundary, Unable to insert");
		}
		else if (size >= array.length){
			resize();
			insert(index, value);
		}
		else{
			for(int j = size; j >= (index - 1); j--){
				array[j+1] = array[j];
			}
			array[index-1] = value;
			size++;
		}
	
	}
	
	public void delete(int index){
		if (index - 1 < 0 || index - 1 > size){
			System.out.println("No value in this position or out of range");
		}
		else{
			for (int i = index - 1; i < size; i++){
				array[i] = array[i+1];
			}
			size--;
		}
	}
	
	public void resize(){
		int[] prolongedArray = new int[array.length + 1];
		int i = 0;
		for(int value : array){
			prolongedArray[i] = array[i];
			i++;
		}
		array = prolongedArray;
	}
	
	public void getAllValue(){
		for(int i = 0; i < size; i++ ){
			System.out.println(array[i]);
		}
	}
	
	
	public static void main(String[] args) {
		var a = new ArrayInsert(20);
		a.add(3);
		a.add(2);
		a.add(1);
		a.insert(1,1);
		a.delete(2);
		a.getAllValue();
	}
}