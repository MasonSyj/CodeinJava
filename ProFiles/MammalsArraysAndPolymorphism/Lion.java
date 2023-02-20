public class Lion extends Mammal implements Pet{

	public Lion(int age, int weight){
		super(age, weight);
	}
	public void makeNoise() {
		System.out.println("grrrrrrr! roar");
	}

	@Override
	public void eat() {
		System.out.println("Lion eat plenty of meat");
		this.weightKg+=10;
	}
}
