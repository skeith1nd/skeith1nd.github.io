package io.github.skeith1nd.network.core;

import playn.core.Json;

public interface INetworkObject {
    public Json.Object serialize();
    public void deserialize(Json.Object json);
    public void reset();
}
