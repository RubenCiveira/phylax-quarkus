package net.civeira.phylax.common.document;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jakarta.activation.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

/**
 * Reads structured records from document sources using readers and transformers.
 *
 * It selects a reader that can handle the input and applies transformations as needed.
 * Transformations allow converting one content type into another before reading. This is used by
 * import workflows that accept multiple file formats. The output is a list of key-value maps
 * representing parsed rows.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class DocumentReader {

  private final Instance<FileReader> readers;

  private final Instance<FileTransformer> transforms;

  /**
   * Reads a document source and returns the extracted records.
   *
   * The method finds a compatible reader and applies transformations when required. Each record is
   * represented as a key-value map aligned to the file schema. This supports batch imports where
   * records are later validated and persisted.
   *
   * @param source document input source
   * @return list of key-value records extracted from the document
   */
  public List<Map<String, String>> read(DataSource source) {
    List<Map<String, String>> result = new ArrayList<>();
    Queue<DataSource> queue = new ArrayDeque<>();
    Set<DataSource> visited = Collections.newSetFromMap(new IdentityHashMap<>());

    queue.add(source);
    while (!queue.isEmpty()) {
      DataSource current = queue.poll();
      if (!visited.add(current)) {
        continue;
      }
      for (FileReader reader : readers) {
        if (reader.canRead(current)) {
          result.addAll(reader.read(current));
        }
      }

      // Si no lo puede leer ningún reader, probamos transformers
      for (FileTransformer transformer : transforms) {
        if (transformer.canTransform(current)) {
          List<DataSource> outDocs = transformer.transform(current);
          queue.addAll(outDocs);
        }
      }
    }
    return result;
  }

}
