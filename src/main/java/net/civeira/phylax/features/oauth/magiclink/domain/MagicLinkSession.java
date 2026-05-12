package net.civeira.phylax.features.oauth.magiclink.domain;

import java.time.Duration;

public record MagicLinkSession(String sessionId, Duration expiration) {
}
