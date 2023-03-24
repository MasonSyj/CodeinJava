public class VetVisitor extends Visitor {
    @Override
    void visit(Dog d) {
        System.out.println("The dog is unhappy about seeing the vet and runs off");
    }

    @Override
    void visit(Dolphin d) {
        System.out.println("The vet assesses the dolphin and refers it to a dental hygienist");
    }

    @Override
    void visit(Lion l) {
        System.out.println("The lion is tranquilised and then given a series of vaccinations");
    }
}
