public class CycleRepublicVisitor extends Visitor {
    @Override
    void visit(BronzeCard d) {
        System.out.println("Bronze card used in Cycle Republic: 0% cash back paid");
    }

    @Override
    void visit(SilverCard d) {
        System.out.println("Silver card used in Cycle Republic: 1% cash back paid");
    }

    @Override
    void visit(GoldCard l) {
        System.out.println("Gold card used in Cycle Republic: 5% cash back paid");
    }
}
