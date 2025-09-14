package net.JavacClassic;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class Config {

  public static ConfigData data;
  private static final Logger logger = LoggerFactory.getLogger(Config.class);

  static {
    try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.yml")
    ) {
      if (in == null) throw new RuntimeException("Config.yml doesn't exist");
      Yaml yaml = new Yaml();
      data = yaml.loadAs(in, ConfigData.class);
    } catch (Exception e) {
      logger.error("Error From YAML CONFIG: ", e);
    }
  }
}
