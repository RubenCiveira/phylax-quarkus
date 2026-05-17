package net.civeira.phylax.features.oauth.gdpr.domain.gateway;

public interface UserDataExportGateway {

  void requestExport(String userUid, String username, String email, String tenant);
}
