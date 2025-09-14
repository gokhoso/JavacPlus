package net.JavacClassic.Utils;

import java.util.List;
import java.util.Optional;
import net.JavacClassic.Handlers.Command.ContextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModerationUtils {

  private static final Logger logger = LoggerFactory.getLogger(
    ModerationUtils.class
  );

  private boolean isPermAvailable(ContextCommand ctx) {
    boolean hasAdmin = ctx
      .getAuthorMember()
      .getPermissions()
      .stream()
      .anyMatch(perm -> perm == Permission.ADMINISTRATOR);
    logger.debug(
      "Checking admin permission for member: " +
      ctx.getAuthorMember().getId() +
      " - Result: " +
      hasAdmin
    );
    return hasAdmin;
  }

  private Optional<Role> getHighestRole(List<Role> roles) {
    Role highestRole = null;
    for (Role role : roles) {
      if (role == null) {
        logger.debug("Encountered null role in roles list.");
        continue;
      }

      if (highestRole == null) {
        highestRole = role;
        logger.debug("Initial highest role set to: " + role.getName());
        continue;
      }

      if (role.isDetached() || highestRole.isDetached()) {
        logger.debug("Skipping detached role: " + role.getName());
        continue;
      }

      if (role.getPosition() > highestRole.getPosition()) {
        logger.debug(
          "Role " +
          role.getName() +
          " (" +
          role.getPosition() +
          ") is higher than " +
          highestRole.getName() +
          " (" +
          highestRole.getPosition() +
          ")"
        );
        highestRole = role;
      }
    }

    logger.info(
      "Highest role determined: " +
      (highestRole != null ? highestRole.getName() : "none")
    );
    return Optional.ofNullable(highestRole);
  }

  private boolean isRolePositionHigherToExecute(ContextCommand ctx) {
    if (ctx.getMessage().getMentions().getMembers().size() == 0) {
      logger.debug("Member mention check failed.");
      return true;
    }

    if (ctx.getMessage().getMentions().getMembers().size() > 1) {
      logger.debug("Member mention check failed.");
      return false;
    }

    Member memberTarget = ctx.getMessage().getMentions().getMembers().get(0);
    Member memberAuthor = ctx.getAuthorMember();

    Role memberTargetHighestRole = getHighestRole(
      memberTarget.getRoles()
    ).orElse(null);

    Role memberAuthorHighestRole = getHighestRole(
      memberAuthor.getRoles()
    ).orElse(null);

    logger.debug(
      "Author highest role: " +
      (memberAuthorHighestRole != null
          ? memberAuthorHighestRole.getName()
          : "none")
    );
    logger.debug(
      "Target highest role: " +
      (memberTargetHighestRole != null
          ? memberTargetHighestRole.getName()
          : "none")
    );

    if (memberTargetHighestRole == null || memberAuthorHighestRole == null) {
      logger.debug("One or both members have no roles.");
      return false;
    }

    boolean result =
      memberAuthorHighestRole.getPosition() >
      memberTargetHighestRole.getPosition();
    logger.debug(
      "Author role position (" +
      memberAuthorHighestRole.getPosition() +
      ") > Target role position (" +
      memberTargetHighestRole.getPosition() +
      "): " +
      result
    );
    return result;
  }

  public boolean canExecute(ContextCommand ctx) {
    logger.debug(
      "Checking if command can be executed by member: " + ctx.getAuthorId()
    );
    if (!isPermAvailable(ctx)) {
      logger.debug("Permission check failed for member: " + ctx.getAuthorId());
      return false;
    }

    boolean canExecute = isRolePositionHigherToExecute(ctx);
    logger.debug("Role position check result: " + canExecute);
    return canExecute;
  }
}
