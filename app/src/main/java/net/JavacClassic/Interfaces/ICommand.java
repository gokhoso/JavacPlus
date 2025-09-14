package net.JavacClassic.Interfaces;

import net.JavacClassic.Entites.CommandInformation;
import net.JavacClassic.Handlers.Command.ContextCommand;

public interface ICommand {
  CommandInformation getInformation();

  void execute(ContextCommand ctx);
}
