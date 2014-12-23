package io.github.skeith1nd.network.core;

import org.json.JSONObject;

public interface INetworkObject {
    public JSONObject serialize();
    public void deserialize(JSONObject json);
    public void reset();
}
