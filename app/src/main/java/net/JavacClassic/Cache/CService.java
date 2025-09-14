package net.JavacClassic.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.JavacClassic.Interfaces.IService;

public class CService {

  Map<String, IService> services = new ConcurrentHashMap<>();

  public void addCache(String name, IService service) {
    services.put(name, service);
  }

  public IService getCache(String name) {
    return services.getOrDefault(name, null);
  }
}
