package net.civeira.phylax.common.document;

import java.util.List;

import jakarta.activation.DataSource;

public interface FileTransformer {
  /**
   * Convert a mimeType on another mime type
   * 
   * @param contentType
   * @return
   */
  boolean canTransform(DataSource source);

  List<DataSource> transform(DataSource source);
}
