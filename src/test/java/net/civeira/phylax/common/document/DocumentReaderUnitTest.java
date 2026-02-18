package net.civeira.phylax.common.document;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.activation.DataSource;
import jakarta.enterprise.inject.Instance;

@DisplayName("DocumentReader file reading orchestrator")
class DocumentReaderUnitTest {

  @Mock
  private Instance<FileReader> readersInstance;

  @Mock
  private Instance<FileTransformer> transformsInstance;

  @Mock
  private FileReader fileReader;

  @Mock
  private FileTransformer fileTransformer;

  @Mock
  private DataSource dataSource;

  private DocumentReader documentReader;
  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    documentReader = new DocumentReader(readersInstance, transformsInstance);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  @DisplayName("read()")
  class ReadMethod {

    @Test
    @DisplayName("Should return data from a reader that can read the source")
    void shouldReturnDataFromReaderThatCanReadSource() {
      // Arrange — Configure a reader that can handle the data source and returns one row
      when(fileReader.canRead(dataSource)).thenReturn(true);
      when(fileReader.read(dataSource)).thenReturn(List.of(Map.of("col1", "val1")));
      mockReaderIterator(List.of(fileReader));
      mockTransformerIterator(List.of());

      // Act — Invoke the document reader to process the data source
      List<Map<String, String>> result = documentReader.read(dataSource);

      // Assert — Verify the reader produced exactly one row with the expected column value
      assertEquals(1, result.size(), "Should return exactly 1 row from the reader");
      assertEquals("val1", result.get(0).get("col1"),
          "The row should contain the expected column value");
    }

    @Test
    @DisplayName("Should return empty list when no reader can read the source")
    void shouldReturnEmptyListWhenNoReaderCanRead() {
      // Arrange — Configure a reader that cannot handle the data source
      when(fileReader.canRead(dataSource)).thenReturn(false);
      mockReaderIterator(List.of(fileReader));
      mockTransformerIterator(List.of());

      // Act — Invoke the document reader with no compatible reader available
      List<Map<String, String>> result = documentReader.read(dataSource);

      // Assert — Verify the result is an empty list when no reader matches
      assertTrue(result.isEmpty(), "Should return empty list when no reader can handle the source");
    }

    @Test
    @DisplayName("Should process transformed sources recursively")
    void shouldProcessTransformedSourcesRecursively() {
      // Arrange — Set up a transformer that converts the original source into a new one the reader
      // can handle
      DataSource transformedSource = mock(DataSource.class);
      when(fileReader.canRead(dataSource)).thenReturn(false);
      when(fileReader.canRead(transformedSource)).thenReturn(true);
      when(fileReader.read(transformedSource)).thenReturn(List.of(Map.of("key", "transformed")));
      when(fileTransformer.canTransform(dataSource)).thenReturn(true);
      when(fileTransformer.transform(dataSource)).thenReturn(List.of(transformedSource));
      when(fileTransformer.canTransform(transformedSource)).thenReturn(false);
      doAnswer(inv -> List.of(fileReader).iterator()).when(readersInstance).iterator();
      doAnswer(inv -> List.of(fileTransformer).iterator()).when(transformsInstance).iterator();

      // Act — Invoke the document reader which should follow the transformation chain
      List<Map<String, String>> result = documentReader.read(dataSource);

      // Assert — Verify data comes from the transformed source after recursive processing
      assertFalse(result.isEmpty(), "Should have results from the transformed source");
      assertEquals("transformed", result.get(0).get("key"),
          "Data should come from the transformed source");
    }

    @Test
    @DisplayName("Should not visit the same source twice")
    void shouldNotVisitSameSourceTwice() {
      // Arrange — Configure a transformer that creates a cycle back to the same data source
      when(fileReader.canRead(dataSource)).thenReturn(true);
      when(fileReader.read(dataSource)).thenReturn(List.of(Map.of("k", "v")));
      when(fileTransformer.canTransform(dataSource)).thenReturn(true);
      when(fileTransformer.transform(dataSource)).thenReturn(List.of(dataSource));
      doAnswer(inv -> List.of(fileReader).iterator()).when(readersInstance).iterator();
      doAnswer(inv -> List.of(fileTransformer).iterator()).when(transformsInstance).iterator();

      // Act — Invoke the document reader with a cyclic transformation
      List<Map<String, String>> result = documentReader.read(dataSource);

      // Assert — Verify the source is processed only once despite the cycle
      assertEquals(1, result.size(),
          "Should process the source only once even if transformer creates a cycle");
    }
  }

  private void mockReaderIterator(List<FileReader> readers) {
    doAnswer(inv -> readers.iterator()).when(readersInstance).iterator();
  }

  private void mockTransformerIterator(List<FileTransformer> transformers) {
    doAnswer(inv -> transformers.iterator()).when(transformsInstance).iterator();
  }
}
