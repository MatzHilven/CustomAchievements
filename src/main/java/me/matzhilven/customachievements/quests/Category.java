package me.matzhilven.customachievements.quests;

import lombok.Getter;

public enum Category {
    MINEBLOCKS("Blocks Mined"),
    FISH("Fish Caught"),
    KILLMOBS("Mobs Killed"),
    KILLPLAYERS("Players Killed"),
    PLAYTIME("Time Played");

    @Getter
    private final String name;

    Category(String name) {
        this.name = name;
    }
}
