package org.ilyaraz.timepuzzle;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ProofOfWork {
  private static final int PASSWORD_LENGTH = 15;
  private static final int NUM_BITS = 1024;
  private static final String PASSWORD_FILE_NAME = "entries.dat";

  private ArrayList<Entry> entries;

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    new ProofOfWork().run(args);
  }

  private void showUsage() {
    System.out.println("Usage: ");
    System.out.println("  java -jar ProofOfWork.jar list");
    System.out.println("  java -jar ProofOfWork.jar generate <exponent> <tag>");
    System.out.println("  java -jar ProofOfWork.jar recover <id>");
  }

  private void run(String[] args) throws IOException, ClassNotFoundException {
    if (args.length == 0) {
      showUsage();
      System.exit(1);
    } else if (Objects.equals(args[0], "list")) {
      loadEntries();
      for (int i = 0; i < entries.size(); i++) {
        System.out.println(i + " -> " + entries.get(i));
      }
    } else if (Objects.equals(args[0], "generate")) {
      long exponent = Long.parseLong(args[1]);
      String tag = args[2];
      Random random = new SecureRandom();
      Password p = new Password(PASSWORD_LENGTH, random);
      System.out.println(p);
      TimePuzzle tp = new TimePuzzle(NUM_BITS, exponent, p.toBigInteger(), random);
      Entry e = new Entry(tag, tp);
      loadEntries();
      entries.add(e);
      saveEntries();
    } else if (Objects.equals(args[0], "recover")) {
      int id = Integer.parseInt(args[1]);
      loadEntries();
      Entry e = entries.get(id);
      Password p = new Password(e.getPuzzle().decode());
      System.out.println(p);
    } else {
      showUsage();
      System.exit(1);
    }
  }

  private void saveEntries() throws IOException {
    File f = new File(PASSWORD_FILE_NAME);
    if (f.exists() && f.isDirectory()) {
      throw new RuntimeException("'" + PASSWORD_FILE_NAME + "' is a directory");
    }
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
    oos.writeObject(entries);
    oos.flush();
    oos.close();
  }

  private void loadEntries() throws IOException, ClassNotFoundException {
    File f = new File(PASSWORD_FILE_NAME);
    if (!f.exists()) {
      entries = new ArrayList<>();
    } else if (f.isDirectory()) {
      throw new RuntimeException("'" + PASSWORD_FILE_NAME + "' is a directory");
    } else {
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
      entries = (ArrayList<Entry>) ois.readObject();
    }
  }
}
