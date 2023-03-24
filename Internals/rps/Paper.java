public class Paper extends Attack{

        public void handleRock(Attack a){ System.out.println("Paper vs rock paper win");}
        public void handlePaper(Attack a){
            System.out.println("Paper vs paper we draw");
        }
        public void handleScissors(Attack a){
            System.out.println("Rock vs scissors paper lose");
        }

        public void handle(Attack a){
            a.handlePaper(this);
        }
    }


