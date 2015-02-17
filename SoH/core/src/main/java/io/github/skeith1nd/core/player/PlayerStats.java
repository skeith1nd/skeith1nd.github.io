package io.github.skeith1nd.core.player;

import playn.core.Json;

public class PlayerStats {
    private double currentHP, maxHP;

    public PlayerStats() {
        currentHP = maxHP = 0;
    }

    public void fromJSON(Json.Object player) {
        currentHP = player.getDouble("currentHP");
        maxHP = player.getDouble("maxHP");
    }

    public void toJSON(Json.Object jsonObject) {
        jsonObject.put("maxHP", maxHP);
        jsonObject.put("currentHP", currentHP);
    }

    public double getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public double getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
