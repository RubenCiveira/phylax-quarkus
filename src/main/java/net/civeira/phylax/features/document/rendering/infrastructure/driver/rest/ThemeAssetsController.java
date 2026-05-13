package net.civeira.phylax.features.document.rendering.infrastructure.driver.rest;

import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.document.theme.domain.Theme;
import net.civeira.phylax.features.document.theme.domain.gateway.ThemeFilter;
import net.civeira.phylax.features.document.theme.domain.gateway.ThemeReadRepositoryGateway;
import net.civeira.phylax.features.document.themeasset.domain.ThemeAsset;
import net.civeira.phylax.features.document.themeasset.domain.gateway.ThemeAssetContentUploadGateway;
import net.civeira.phylax.features.document.themeasset.domain.gateway.ThemeAssetFilter;
import net.civeira.phylax.features.document.themeasset.domain.gateway.ThemeAssetReadRepositoryGateway;

/**
 * Public endpoint that serves static assets (CSS, images, fonts) belonging to a named theme.
 *
 * <p>
 * URL pattern: {@code GET /oauth/themes/{themeName}/{assetCode}}
 *
 * <p>
 * No authentication is required — assets are public resources referenced by rendered HTML pages.
 * The {@code theme_assets_path} Handlebars variable injected by {@code DecorateHtml} points here.
 */
@Path("")
@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class ThemeAssetsController {

  private final ThemeReadRepositoryGateway themes;
  private final ThemeAssetReadRepositoryGateway themeAssets;
  private final ThemeAssetContentUploadGateway contentStore;

  @GET
  @Path("oauth/themes/{themeName}/{assetCode}")
  public Response serveAsset(@PathParam("themeName") String themeName,
      @PathParam("assetCode") String assetCode) {

    Optional<Theme> theme =
        themes.find(ThemeFilter.builder().name(themeName).build());
    if (theme.isEmpty()) {
      log.debug("Theme '{}' not found", themeName);
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    Optional<ThemeAsset> asset = themeAssets
        .find(ThemeAssetFilter.builder().theme(theme.get()).code(assetCode).build());
    if (asset.isEmpty()) {
      log.debug("Asset '{}' not found in theme '{}'", assetCode, themeName);
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    return contentStore.readContent(asset.get()).map(content -> {
      String mediaType =
          content.getContentType() != null ? content.getContentType() : guessMediaType(assetCode);
      CacheControl cc = new CacheControl();
      cc.setMaxAge(3600);
      return Response.ok(content.getInputStream(), mediaType).cacheControl(cc).build();
    }).orElseGet(() -> {
      log.debug("No binary content stored for asset '{}' in theme '{}'", assetCode, themeName);
      return Response.status(Response.Status.NOT_FOUND).build();
    });
  }

  private static String guessMediaType(String filename) {
    if (filename.endsWith(".css")) return "text/css";
    if (filename.endsWith(".js")) return "application/javascript";
    if (filename.endsWith(".png")) return "image/png";
    if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
    if (filename.endsWith(".svg")) return "image/svg+xml";
    if (filename.endsWith(".gif")) return "image/gif";
    if (filename.endsWith(".ico")) return "image/x-icon";
    if (filename.endsWith(".woff")) return "font/woff";
    if (filename.endsWith(".woff2")) return "font/woff2";
    if (filename.endsWith(".ttf")) return "font/ttf";
    return "application/octet-stream";
  }
}
