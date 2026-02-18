package net.civeira.phylax.common.infrastructure.mail;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Base64;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.activation.DataSource;

@DisplayName("Attach email attachment model")
class AttachUnitTest {

  @Nested
  @DisplayName("Constructor with File")
  class ConstructorWithFile {

    @Test
    @DisplayName("Should create attachment with file and no datasource")
    void shouldCreateAttachmentWithFileAndNoDatasource() {
      // Arrange — Prepare a File reference for the attachment
      File file = new File("/tmp/test.pdf");

      // Act — Construct an Attach using the File-based constructor
      Attach attach = new Attach("test.pdf", file);

      // Assert — Verify the name matches and File is present while DataSource is empty
      assertEquals("test.pdf", attach.getName(), "Name should match the provided value");
      assertTrue(attach.getFichero().isPresent(),
          "File should be present when constructed with File");
      assertTrue(attach.getDatasource().isEmpty(),
          "DataSource should be empty when constructed with File");
    }
  }

  @Nested
  @DisplayName("Constructor with DataSource")
  class ConstructorWithDataSource {

    @Test
    @DisplayName("Should create attachment with datasource and no file")
    void shouldCreateAttachmentWithDatasourceAndNoFile() {
      // Arrange — Create a mock DataSource for the attachment
      DataSource ds = mock(DataSource.class);

      // Act — Construct an Attach using the DataSource-based constructor
      Attach attach = new Attach("report.csv", ds);

      // Assert — Verify the name matches and DataSource is present while File is empty
      assertEquals("report.csv", attach.getName(), "Name should match the provided value");
      assertTrue(attach.getDatasource().isPresent(),
          "DataSource should be present when constructed with DataSource");
      assertTrue(attach.getFichero().isEmpty(),
          "File should be empty when constructed with DataSource");
    }
  }

  @Nested
  @DisplayName("getCid()")
  class GetCid {

    @Test
    @DisplayName("Should return base64 encoded name as CID")
    void shouldReturnBase64EncodedNameAsCid() {
      // Arrange — Create an attachment and compute the expected Base64-encoded CID
      File file = new File("/tmp/logo.png");
      Attach attach = new Attach("logo.png", file);
      String expectedCid = Base64.getEncoder().encodeToString("logo.png".getBytes());

      // Act — Retrieve the CID from the attachment
      String cid = attach.getCid();

      // Assert — Verify the CID matches the Base64-encoded name
      assertEquals(expectedCid, cid, "CID should be the Base64 encoded name of the attachment");
    }

    @Test
    @DisplayName("Should produce different CIDs for different names")
    void shouldProduceDifferentCidsForDifferentNames() {
      // Arrange — Create two attachments with different names
      File file = new File("/tmp/dummy");
      Attach attach1 = new Attach("file1.pdf", file);
      Attach attach2 = new Attach("file2.pdf", file);

      // Act — Retrieve the CIDs from both attachments
      String cid1 = attach1.getCid();
      String cid2 = attach2.getCid();

      // Assert — Verify that different names produce different CIDs
      assertNotEquals(cid1, cid2, "Different attachment names should produce different CIDs");
    }
  }
}
