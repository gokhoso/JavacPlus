package net.JavacClassic.Security;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

  private final Map<String, Integer> cooldowns = new HashMap<>();
  private final Map<String, Integer> ratelimits = new HashMap<>();

  private final ScheduledExecutorService scheduler =
    Executors.newScheduledThreadPool(2);

  public void addCooldown(String userID, int duration) {
    if (hasCooldown(userID) || isRateLimited(userID)) return;
    cooldowns.put(userID, duration);

    scheduler.schedule(
      () -> removeCooldown(userID),
      duration,
      TimeUnit.SECONDS
    );
  }

  public boolean isRateLimited(String userID) {
    System.err.println(ratelimits.containsKey(userID));
    return ratelimits.containsKey(userID);
  }

  public void addRatelimit(String userID, Integer duration) {
    if (isRateLimited(userID)) return;

    ratelimits.put(userID, duration);

    scheduler.schedule(
      () -> removeRateLimit(userID),
      duration,
      TimeUnit.SECONDS
    );
  }

  public void removeRateLimit(String userID) {
    if (!isRateLimited(userID)) return;
    removeCooldown(userID);
    ratelimits.remove(userID);
  }

  public boolean hasCooldown(String userID) {
    return cooldowns.containsKey(userID);
  }

  public Integer getCooldown(String userID) {
    if (!hasCooldown(userID)) return null;

    return cooldowns.get(userID);
  }

  public void removeCooldown(String userID) {
    if (!hasCooldown(userID)) return;
    System.err.println("Removing...");
    cooldowns.remove(userID);
  }
}
