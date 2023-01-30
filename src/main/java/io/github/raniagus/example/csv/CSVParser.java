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

public class CSVParser {
  private File file;
  private String separator;
  private ObjectMapper objectMapper;

  public CSVParser(String filename, String separator) {
    this.file = new File(filename);
    this.separator = separator;
    this.objectMapper = new ObjectMapper();
  }

  public <T> Stream<T> parse(Class<T> clazz) {
    try (var scanner = new Scanner(file)) {
      var streamBuilder = Stream.<Map<String, String>>builder();
      var header = scanner.nextLine().split(separator);

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
    } catch (FileNotFoundException e) {
      throw new UncheckedIOException(e);
    }
  }
}
