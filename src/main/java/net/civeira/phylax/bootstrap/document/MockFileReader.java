package net.civeira.phylax.bootstrap.document;

import java.util.List;
import java.util.Map;

import jakarta.activation.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.common.document.FileReader;

@ApplicationScoped
public class MockFileReader implements FileReader {

  @Override
  public boolean canRead(DataSource source) {
    return true;
  }

  @Override
  public List<Map<String, String>> read(DataSource filename) {
    return List.of(Map.of("name", "quelem"), Map.of("name", "joanh"));
  }

}
