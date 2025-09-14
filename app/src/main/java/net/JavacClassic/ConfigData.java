package net.JavacClassic;

import java.util.List;

public class ConfigData {

  public Bot bot;
  public Config config;
  public Cache cache;
  public Channels channels;
  public Roles roles;

  public static class Bot {
    public String basePackage;
    public String lang;
    public String guild_id;
    public String prefix;
    public List<String> owners;
  }

  public static class Config {

    public int service_pool;
  }

  public static class Cache {

    public int max_message;
    public int componentTTL;
  }

  public static class Channels {

    public String general;
    public String rule;
    public String log;
    public String count;
    public String rice;
  }

  public static class Roles {

    public String member;
  }
}
