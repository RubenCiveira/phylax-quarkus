package net.civeira.phylax.common.document;

import java.util.List;
import java.util.Map;

import jakarta.activation.DataSource;

public interface FileReader {

  boolean canRead(DataSource source);

  List<Map<String, String>> read(DataSource source);
}
