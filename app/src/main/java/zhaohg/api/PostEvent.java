package zhaohg.api;

import zhaohg.json.JsonValue;

public interface PostEvent {

    public void onPostEvent(JsonValue json);

}
