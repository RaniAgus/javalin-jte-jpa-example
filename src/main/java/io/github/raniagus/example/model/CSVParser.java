package io.github.raniagus.example.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;

public class CSVParser implements AutoCloseable {
  private Scanner scanner;
  private String separator;

  public CSVParser(String filename, String separator) {
    try {
      this.scanner = new Scanner(new File(filename));
      this.separator = separator;
      scanner.nextLine(); // skip header
    } catch (FileNotFoundException e) {
      throw new UncheckedIOException(e);
    }
  }

  public <T> Stream<T> parse(Function<String[], T> consumer) {
    var streamBuilder = Stream.<T>builder();
    while (scanner.hasNextLine()) {
      streamBuilder.add(consumer.apply(scanner.nextLine().split(separator)));
    }
    return streamBuilder.build();
  }

  @Override
  public void close() {
    scanner.close();
  }
}
