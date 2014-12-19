package io.github.skeith1nd.network.core.commands;

import io.github.skeith1nd.network.core.INetworkObject;

public abstract class Command implements INetworkObject {
    protected int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
