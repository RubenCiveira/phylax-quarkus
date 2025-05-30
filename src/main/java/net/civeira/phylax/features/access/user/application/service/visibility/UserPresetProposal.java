package net.civeira.phylax.features.access.user.application.service.visibility;

import java.util.Optional;
import java.util.function.Consumer;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.user.User;
import net.civeira.phylax.features.access.user.application.request.UserStateChange;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPresetProposal {

  /**
   * @autogenerated PresetProposalGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated PresetProposalGenerator
   */
  @NonNull
  private UserStateChange dto;

  /**
   * @autogenerated PresetProposalGenerator
   */
  private User original;

  /**
   * @autogenerated PresetProposalGenerator
   * @return
   */
  public Optional<User> getOriginal() {
    return Optional.ofNullable(original);
  }

  /**
   * @autogenerated PresetProposalGenerator
   * @param mapper
   */
  public void peek(Consumer<UserStateChange> mapper) {
    mapper.accept(dto);
  }
}
