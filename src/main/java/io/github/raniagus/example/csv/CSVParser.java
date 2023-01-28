package io.github.raniagus.example.csv;

import static java.lang.Math.min;
import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CSVParser implements AutoCloseable {
  private Scanner scanner;
  private String separator;
  private String[] header;
  private ObjectMapper objectMapper;

  public CSVParser(String filename, String separator) {
    try {
      this.scanner = new Scanner(new File(filename));
      this.separator = separator;
      this.header = scanner.nextLine().split(separator);
      this.objectMapper = new ObjectMapper();
    } catch (FileNotFoundException e) {
      throw new UncheckedIOException(e);
    }
  }

  public <T> Stream<T> parse(Class<T> clazz) {
    var streamBuilder = Stream.<Map<String, String>>builder();
    while (scanner.hasNextLine()) {
      var line = scanner.nextLine().split(separator);
      var tuple = new String[header.length];

      fill(tuple, "");
      arraycopy(line, 0, tuple, 0, min(tuple.length, line.length));

      streamBuilder.add(IntStream.range(0, header.length)
          .mapToObj(i -> Map.entry(header[i], tuple[i]))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
    return streamBuilder.build().map(map -> objectMapper.convertValue(map, clazz));
  }

  @Override
  public void close() {
    scanner.close();
  }
}
