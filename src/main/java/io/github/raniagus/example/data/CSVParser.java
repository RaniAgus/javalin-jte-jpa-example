package io.github.raniagus.example.data;

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
      streamBuilder.add(IntStream.range(0, line.length)
          .mapToObj(i -> Map.entry(header[i], line[i]))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
    return streamBuilder.build().map(map -> objectMapper.convertValue(map, clazz));
  }

  @Override
  public void close() {
    scanner.close();
  }
}
