package io.github.skeith1nd.network.core;

import com.google.gwt.json.client.JSONObject;

public interface INetworkObject {
    public JSONObject serialize();
    public void deserialize(JSONObject json);
    public void reset();
}
