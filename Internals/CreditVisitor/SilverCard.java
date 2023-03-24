public class SilverCard extends AbstractCard {
    @Override
    public void stateAPR() {
        System.out.println("18%");
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
