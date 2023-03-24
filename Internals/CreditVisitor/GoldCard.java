public class GoldCard extends AbstractCard {
    @Override
    public void stateAPR() {
        System.out.println("14%");
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
