package javass;

public class Card { 
	public static void main(String[] args) {
	
		
	}
	
	class AllCards{
		private OneCard[] cards;
		private int cardNumbers;
		
		public AllCards(int cardNumbers){
			this.cardNumbers = cardNumbers;
			OneCard[] cards = new OneCard[cardNumbers];
		}
		public void shuffle(){}
		public oneCard getTop(){}
		public void draw(){}
	}
	
	class OneCard {
		private int value;
		private int suit;  //大概是花色的意思
		//一个构造器，还有两个get方法
		public OneCard(int value, int suit){
			this.value = value;
			this.suit = suit;
		}
		
		public int getValue(){
			return value;
		}
		
		public int getSuit(){
			return suit;
		}
	}
	
}