package zhaohg.json;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class JsonObject extends JsonValue {

    private SortedMap<JsonString, JsonValue> data;

    public JsonObject() {
        this.data = new TreeMap<JsonString, JsonValue>();
    }

    public JsonObject(SortedMap<JsonString, JsonValue> value) {
        this.data = value;
    }

    public JsonObject(Map<JsonString, JsonValue> value) {
        this.data = new TreeMap<JsonString, JsonValue>();
        for (Map.Entry<JsonString, JsonValue> entry : value.entrySet()) {
            this.putValue(entry.getKey(), entry.getValue());
        }
    }

    public void putValue(String key, JsonValue value) {
        this.putValue(new JsonString(key), value);
    }

    public void putValue(JsonString key, JsonValue value) {
        this.data.put(key, value);
    }

    @Override
    public boolean tryParse(String json, int offset) {
        int index = this.skipWhitespaces(json, offset);
        if (index == json.length() || json.charAt(index) != '{') {
            return false;
        }
        ++index;
        this.data = new TreeMap<JsonString, JsonValue>();
        while (true) {
            JsonString jsonString = new JsonString();
            if (!jsonString.tryParse(json, index)) {
                break;
            }
            index = jsonString.getNewOffset();
            index = this.skipWhitespaces(json, index);
            if (index == json.length() || json.charAt(index) != ':') {
                return false;
            }
            ++index;
            JsonValue jsonValue = new JsonValue();
            if (!jsonValue.tryParse(json, index)) {
                return false;
            }
            index = jsonValue.getNewOffset();
            index = this.skipWhitespaces(json, index);
            if (index == json.length()) {
                return false;
            }
            if (this.data.containsKey(jsonString)) {
                return false;
            }
            this.data.put(jsonString, jsonValue);
            if (json.charAt(index) != ',') {
                break;
            }
            ++index;
        }
        index = this.skipWhitespaces(json, index);
        if (json.charAt(index) != '}') {
            return false;
        }
        this.setNewOffset(index + 1);
        return true;
    }

    @Override
    public String toString() {
        String json = "{";
        boolean first = true;
        for (Map.Entry<JsonString, JsonValue> entry : this.data.entrySet()) {
            if (first) {
                first = false;
            } else {
                json += ",";
            }
            json += entry.getKey().toString() + ":" + entry.getValue().toString();
        }
        json += "}";
        return json;
    }

    public Map<JsonString, JsonValue> getObject() {
        return this.data;
    }

    public JsonValue getValue(String key) {
        return this.data.get(new JsonString(key));
    };

}
