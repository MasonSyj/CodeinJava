public class GroomingVisitor extends Visitor {
    @Override
    void visit(Dog d) {
        System.out.println("The dog looks great. Everyone is happy");
    }

    @Override
    void visit(Dolphin d) {
        System.out.println("The groomer has forgotten their swimming stuff");
    }

    @Override
    void visit(Lion l) {
        System.out.println("The groomer narrowly escapes intact");
    }
}
