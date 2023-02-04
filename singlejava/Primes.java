/* Generate primes up to a maximum using
http://en.wikipedia.org/wiki/Sieve_of_Eratosthenes */

import java.util.BitSet;

class Primes {
  private int max;
  private BitSet crossedOut;

  public static void main(String[] args) {
    Primes program = new Primes();
    program.run(args);
  }

  void run(String[] args) {
      if (args.length == 0) { test(); return; }
      findMax(args);
      uncrossAll();
      crossOutMultiples();
      reportUncrossed();
   }

  void findMax(String[] args) {
    if (args.length != 1) fail("Use: java Primes max");
    try { max = Integer.parseInt(args[0]); }
    catch (Exception err) { fail("Int required"); }
  }

  void fail(String message) {
    System.err.println(message);
    System.exit(1);
  }

  void uncrossAll() {
    crossedOut = new BitSet(max+1);
    for (int i=2; i<=max; i++)
        crossedOut.clear(i);
  }

  void crossOutMultiples() {
    int limit = findIterationLimit();
    for (int i=2; i<=limit; i++) {
      if (crossedOut.get(i)) continue;
      crossOutMultiplesOf(i);
    }
  }

  // Every multiple has a prime factor <= sqrt(max)
  // so we only need to cross out multiples up to sqrt(max)
  int findIterationLimit() {
    double root = Math.sqrt(max);
    return (int) root;
  }

  void crossOutMultiplesOf(int i) {
    for (int m = 2*i; m <= max; m += i) {
      crossedOut.set(m);
    }
  }

  void reportUncrossed() {
    for (int i=2; i<=max; i++) {
      if (crossedOut.get(i)) continue;
      System.out.println(i);
    }
  }

  void test() {
      boolean O = false, I = true;
      boolean[] expected = {I,I,O,O,I,O,I,O,I,I,I,O};
      max = 12;
      uncrossAll();
      crossOutMultiples();
      for (int i = 0; i < max; i++) {
          assert(crossedOut.get(i) != expected[i]);
      }
      System.out.println("Tests pass");
  }
}
