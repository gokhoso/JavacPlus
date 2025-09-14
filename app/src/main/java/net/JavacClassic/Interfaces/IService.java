package net.JavacClassic.Interfaces;

import java.util.concurrent.ScheduledExecutorService;

public interface IService {
  Runnable service();
  void run(ScheduledExecutorService executor);
}
