public class Rock extends Attack {

    public void handleRock(Attack a){
       System.out.println("Rock vs rock we draw");
    }
    public void handlePaper(Attack a){
        System.out.println("Rock vs paper rock lose");
    }
    public void handleScissors(Attack a){
        System.out.println("Rock vs scissors rock win");
    }

    public void handle(Attack a){
        a.handleRock(this);
    }
}
