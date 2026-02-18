package net.civeira.phylax.common.document;

import java.util.List;

import jakarta.activation.DataSource;

/**
 * Converts document data sources into alternative formats.
 *
 * Transformers allow preprocessing such as unzipping, format conversion, or normalization. They are
 * applied before a {@link FileReader} parses the final content. This enables a pipeline approach
 * for complex document formats. Implementations should be deterministic and avoid side effects.
 */
public interface FileTransformer {
  /**
   * Determines whether this transformer can process the given data source.
   *
   * Implementations typically inspect content type or metadata. Returning true indicates the
   * transformer can produce usable outputs. This method should be fast and free of side effects.
   *
   * @param source document input source
   * @return true when the source can be transformed
   */
  boolean canTransform(DataSource source);

  /**
   * Transforms the given source into one or more data sources in other formats.
   *
   * The output list is consumed by {@link DocumentReader} to select readers. Implementations may
   * return multiple sources when a container is expanded. Callers should handle empty outputs as a
   * transformation failure.
   *
   * @param source document input source
   * @return list of transformed data sources
   */
  List<DataSource> transform(DataSource source);
}
