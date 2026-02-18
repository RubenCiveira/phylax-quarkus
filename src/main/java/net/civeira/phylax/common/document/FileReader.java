package net.civeira.phylax.common.document;

import java.util.List;
import java.util.Map;

import jakarta.activation.DataSource;

/**
 * Reads structured records from a document data source.
 *
 * Implementations understand a specific file format or content type. They parse the source into a
 * list of key-value rows. This interface is used by {@link DocumentReader} to select a reader.
 * Readers should avoid side effects and leave validation to callers.
 */
public interface FileReader {

  /**
   * Determines whether this reader can process the given data source.
   *
   * Implementations typically inspect content type or file metadata. Returning true indicates the
   * reader can parse the provided source. This method should be fast and free of side effects.
   *
   * @param source document input source
   * @return true when the source can be read
   */
  boolean canRead(DataSource source);

  /**
   * Reads the provided source and returns the extracted records.
   *
   * Each record is represented as a map of column names to values. Implementations should parse and
   * normalize values as needed. Validation errors should be handled by the caller.
   *
   * @param source document input source
   * @return list of key-value records extracted from the source
   */
  List<Map<String, String>> read(DataSource source);
}
