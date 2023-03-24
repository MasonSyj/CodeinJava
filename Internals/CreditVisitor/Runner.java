public class Runner {
    public static void main(String [] args){
        AbstractCard cBronze = new BronzeCard();
        AbstractCard cSilver = new SilverCard();
        AbstractCard cGold = new GoldCard();

        Visitor vFuel = new FuelVisitor();
        Visitor vTesco = new TescoVisitor();
        Visitor vCycle = new CycleRepublicVisitor();

        //Single dispatch
        cBronze.stateAPR();
        cSilver.stateAPR();
        cGold.stateAPR();

        //Where card and offers meet:
        cBronze.accept(vFuel);
        cBronze.accept(vTesco);
        cBronze.accept(vCycle);

        cSilver.accept(vFuel);
        cSilver.accept(vTesco);
        cSilver.accept(vCycle);

        cGold.accept(vFuel);
        cGold.accept(vTesco);
        cGold.accept(vCycle);


    }
}
