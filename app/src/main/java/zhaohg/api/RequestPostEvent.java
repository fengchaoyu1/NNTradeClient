package zhaohg.api;

import zhaohg.json.JsonValue;

public interface RequestPostEvent {

    public void onPostEvent(JsonValue json);

}
