public class Scissors extends Attack {

        public void handleRock(Attack a){
            System.out.println("Scissors vs rock scissors lose");
        }
        public void handlePaper(Attack a){
            System.out.println("Scissors vs paper scissors win");
        }
        public void handleScissors(Attack a){
            System.out.println("Scissors vs scissors we draw");
        }

        public void handle(Attack a){
            a.handleScissors(this);
        }


}
