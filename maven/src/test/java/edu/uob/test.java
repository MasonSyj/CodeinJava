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

	@Test
	public void typecheck(){
		boolean x1 = Character.isDigit(45);
		boolean x2 = Character.isLetter('z');
		boolean x3 = Character.isLetterOrDigit('.');
		boolean x4 = Character.isLowerCase('a');
		boolean x5 = Character.isUpperCase('e');
		boolean x6 = Character.isWhitespace(' ');
		System.out.println(x1 + " " + x2 + " " + x3 + " " + x4 + " " + x5 + " " + x6);

		char c1 = Character.toLowerCase('A');
		char c2 = Character.toLowerCase('X');
		char c3 = Character.toLowerCase('Z');
		char c4 = Character.toUpperCase('w');
		char c5 = Character.toUpperCase('h');
		char c6 = Character.toUpperCase('k');
		System.out.println(c1 + " " + c2 + " " + c3 + " " + c4 + " " + c5 + " " + c6);
	}

}
