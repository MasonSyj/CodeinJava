public class TescoVisitor extends Visitor {
    @Override
    void visit(BronzeCard d) {
        System.out.println("Bronze card used in Tesco: 0.5% cash back paid");
    }

    @Override
    void visit(SilverCard d) {
        System.out.println("Silver card used in Tesco: 1% cash back paid");
    }

    @Override
    void visit(GoldCard l) {
        System.out.println("Gold card used in Tesco: 2.5% cash back paid");
    }
}
