package edu.uob;

import org.junit.jupiter.api.Test;

import static edu.uob.weekone.isPrime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class test {
	@Test
	public void testIsPrime(){
		assertEquals(true, isPrime(7));
		assertEquals(true, isPrime(11));
		assertEquals(true, isPrime(17));
		assertEquals(false, isPrime(88));
		assertEquals(false, isPrime(16));
		assertEquals(false, isPrime(2));
		assertEquals(false, isPrime(4));
	}

	@Test
	public void testAssert(){
		assert(3 <= 5);
		assert(4 <= 6);
	}
}
