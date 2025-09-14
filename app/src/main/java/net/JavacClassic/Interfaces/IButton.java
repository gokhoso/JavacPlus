package net.JavacClassic.Interfaces;

import net.JavacClassic.Handlers.Component.ComponentContext;

public interface IButton {
  String[] getInformation();

  void execute(ComponentContext ctx);
}
