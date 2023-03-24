public class BronzeCard extends AbstractCard {
    @Override
    public void stateAPR() {
        System.out.println("24%");
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
