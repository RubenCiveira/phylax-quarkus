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

@ApplicationScoped
@RequiredArgsConstructor
public class DocumentReader {

  private final Instance<FileReader> readers;

  private final Instance<FileTransformer> transforms;

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
