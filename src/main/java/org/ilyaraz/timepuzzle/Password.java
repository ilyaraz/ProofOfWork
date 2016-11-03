package org.ilyaraz.timepuzzle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Password {
  private static final int FROM_CHAR = 33;
  private static final int TO_CHAR = 127;

  private int[] pass;

  public Password(int length, Random random) {
    pass = new int[length];
    for (int i = 0; i < length; i++) {
      pass[i] = FROM_CHAR + random.nextInt(TO_CHAR - FROM_CHAR);
    }
  }

  public Password(BigInteger raw) {
    ArrayList<Integer> buf = new ArrayList<>();
    while (raw.compareTo(BigInteger.ZERO) > 0) {
      buf.add(raw.mod(BigInteger.valueOf(TO_CHAR)).intValue());
      raw = raw.divide(BigInteger.valueOf(TO_CHAR));
    }
    for (int x : buf) {
      if (x < FROM_CHAR) {
        throw new RuntimeException("invalid value: " + x);
      }
    }
    pass = new int[buf.size()];
    for (int i = 0; i < pass.length; i++) {
      pass[i] = buf.get(buf.size() - i - 1);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int x : pass) {
      sb.append((char) x);
    }
    return sb.toString();
  }

  public BigInteger toBigInteger() {
    BigInteger result = BigInteger.ZERO;
    for (int x : pass) {
      result = result.multiply(BigInteger.valueOf(TO_CHAR));
      result = result.add(BigInteger.valueOf(x));
    }
    return result;
  }
}
