public class DogPlanet {

	public static void main(String [] args) {	
		
		Dog [] dogArrayObject = new Dog[3];
			
		for (int i = 0; i < dogArrayObject.length; ++i)
		{
			dogArrayObject[i] = new Dog(5, 10);
		}		
		
		for(Dog mCurr : dogArrayObject)
		{
			mCurr.stateAttributes();
			mCurr.makeNoise();
		}
		
	}	
}
