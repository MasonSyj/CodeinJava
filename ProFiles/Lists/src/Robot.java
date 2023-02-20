class Robot {
	
  String name;
  int numLegs;
  float powerLevel;

  public Robot(String name) {
    this.name = name;
    numLegs = 2;
    powerLevel = 2.0f;
  }
  
  Robot() {
    this("Standard Model");
  }

  Robot(String name, int numLegs, float powerLevel){
     this.name = name;
     this.numLegs = numLegs;
     this.powerLevel = powerLevel;
  }

  public void toDefaultString(){
    System.out.printf("This robot's name: %s, numoflegs: %d, powerlevel: %f\n", this.name, this.numLegs, this.powerLevel);
  }

  void talk(String phrase) {
    if (powerLevel >= 1.0f) {
      System.out.println(name + " says " + phrase);
      powerLevel -= 1.0f;
    } else {
      System.out.println(name + " is too weak to talk.");
    }
  }

  void charge(float amount) {
    System.out.println(name + " charges.");
    powerLevel = powerLevel + amount;
  }
}