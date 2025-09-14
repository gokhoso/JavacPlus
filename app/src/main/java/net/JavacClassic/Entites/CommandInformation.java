package net.JavacClassic.Entites;

import net.JavacClassic.Enums.CommandCategory;

public class CommandInformation {
    private final String name;
    private final String desc;
    private final CommandCategory category;
    private final int cooldown;

    public CommandInformation(String name, String desc, CommandCategory category, int cooldown) {
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.cooldown = cooldown;
    }

    public String getName() {return name;}
    public String getDesc() {return desc;}
    public CommandCategory getCategory() {return category;}
    public Integer getCooldown() {return cooldown;}
}
