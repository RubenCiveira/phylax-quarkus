package net.civeira.phylax.bootstrap.document;

import java.util.List;
import java.util.Map;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.activation.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.common.document.FileReader;

/**
 * Dev-only stub for {@link FileReader} that always returns two hardcoded entries.
 *
 * <p>
 * This bean is activated only in the {@code dev} build profile. It must never be active in
 * production; a real implementation should be provided for any other profile.
 * </p>
 */
@IfBuildProfile("dev")
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
