import java.util.*;




public class Tweet {
		
		private final String author;
		private final String text;
		private final Date timestamp;
		private final double[] sides;

		
		public Tweet(String author, String text, Date timestamp) {
			this.author = author;
			this.text = text;
			this.timestamp = timestamp;
			sides = new double[] {3, 4};
		}
		
		/**
		* @return Twitter user who wrote the tweet
		*/
		public String getAuthor() {
			return author;
		}
		
		/**
		* @return text of the tweet
		*/
		public String getText() {
			return text;
		}
		
		/**
		* @return date/time when the tweet was sent
		*/
		public Date getTimestamp() {
			return timestamp;
		}
	
		public double[] getAllSides() {
			return sides;
		}
	
		public static List<Tweet> tweetEveryHourToday () {
			List<Tweet> list = new ArrayList<Tweet>();
			Date date = new Date();
			for (int i = 0; i < 24; i++) {
				date.setHours(i);
				list.add(new Tweet("rbmllr", "keep it up! you can do it", date));
			} 
			return list;
		}
	
		public static void main(String[] args) {
			
			/**
			* @return a list of 24 inspiring tweets, one per hour today
			*/
			List<Tweet> x = Tweet.tweetEveryHourToday();
//			for (Tweet t : x){
//				System.out.println(t.getTimestamp());
//			}
			Date d1 = new Date();
			Tweet t1 = new Tweet("mason", "Hello World", d1);
			var a1 = t1.getAllSides();
			for (double value : a1){
				System.out.println(value);
			}
			
			a1[0] = 30;
			a1[1] = 40;
			
			for (double value : a1){
				System.out.println(value);
			}
		}
}