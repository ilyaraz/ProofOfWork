package org.ilyaraz.timepuzzle;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class TimePuzzle implements Serializable {
    private static final int NUM_ATTEMPTS = 50;

    private final BigInteger n;
    private final BigInteger scrambledValue;
    private final long exponent;

    public TimePuzzle(int numBits, long exponent, BigInteger secretValue, Random random) {
        this.exponent = exponent;
        BigInteger p = genPrime(numBits, random);
        BigInteger q = genPrime(numBits, random);
        this.n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger reduced = BigInteger.valueOf(2).modPow(BigInteger.valueOf(exponent), phi);
        BigInteger shift = BigInteger.valueOf(2).modPow(reduced, n);
        this.scrambledValue = shift.add(secretValue);
    }

    private static BigInteger genPrime(int numBits, Random random) {
        for (;;) {
            BigInteger cur = new BigInteger(numBits, random);
            if (cur.isProbablePrime(NUM_ATTEMPTS)) {
                return cur;
            }
        }
    }

    public BigInteger decode() {
        BigInteger result = BigInteger.valueOf(2);
        for (long i = 0; i < exponent; i++) {
            result = result.multiply(result).mod(n);
            if (i % 1000000 == 0) {
                System.out.println(i + "/" + exponent);
            }
        }
        return scrambledValue.subtract(result);
    }

    public long getExponent() {
        return exponent;
    }
}
