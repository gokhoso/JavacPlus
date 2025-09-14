package net.JavacClassic;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import net.JavacClassic.Cache.CService;
import net.JavacClassic.Interfaces.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceDaemon {

  private final Logger logger = LoggerFactory.getLogger(ServiceDaemon.class);
  private final ScheduledExecutorService scheduledExecutorService;
  private final CService cService;

  public ServiceDaemon(int poolsize, CService cService) {
    this.scheduledExecutorService = new ScheduledThreadPoolExecutor(poolsize);
    this.cService = cService;
  }

  public void addService(String name, IService service) {
    cService.addCache(name, service);
  }

  public IService getService(String name) {
    return cService.getCache(name);
  }

  public void start(String name) {
    IService service = cService.getCache(name);

    if (service == null) {
        logger.info("Service is NULL : {}", name);
      return;
    }

    service.run(scheduledExecutorService);
  }
}
