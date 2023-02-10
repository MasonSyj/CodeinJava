public class Dog extends Mammal implements Pet{

	public Dog(int age, int weight){
		super(age, weight);
	}
	public void makeNoise() {
		System.out.println("woof woof");
	}

	@Override
	public void eat() {
		System.out.println("Dog eat dogfood");
		this.weightKg++;
	}

	public void sit(){
		System.out.println("Dog sit");
	}
}
