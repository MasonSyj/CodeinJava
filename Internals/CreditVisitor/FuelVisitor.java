public class FuelVisitor extends Visitor {
    @Override
    void visit(BronzeCard d) {
        System.out.println("Bronze card used for fuel: 1% cash back paid");
    }

    @Override
    void visit(SilverCard d) {
        System.out.println("Silver card used for fuel: 2% cash back paid");
    }

    @Override
    void visit(GoldCard l) {
        System.out.println("Gold card used for fuel: 3% cash back paid");
    }
}
