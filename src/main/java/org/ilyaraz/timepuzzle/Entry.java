package org.ilyaraz.timepuzzle;

import java.io.Serializable;
import java.util.Date;

public class Entry implements Serializable {
  private final Date creationDate;
  private final String tag;
  private final TimePuzzle puzzle;

  public Entry(String tag, TimePuzzle puzzle) {
    this.creationDate = new Date();
    this.tag = tag;
    this.puzzle = puzzle;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public String getTag() {
    return tag;
  }

  public TimePuzzle getPuzzle() {
    return puzzle;
  }

  @Override
  public String toString() {
    return creationDate + " -> " + tag + " -> " + puzzle.getExponent();
  }
}
