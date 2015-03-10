package zhaohg.json;

import java.util.ArrayList;
import java.util.List;

public class JsonArray extends JsonValue {
    
    private List<JsonValue> data; 
    
    public JsonArray() {
        this.data = new ArrayList<JsonValue>();
    }
    
    public JsonArray(List<JsonValue> value) {
        this.data = value;
    }
    
    public void addValue(JsonValue value) {
        this.data.add(value);
    }

    @Override
    public boolean tryParse(String json, int offset) {
        int index = this.skipWhitespaces(json, offset);
        if (index == json.length() || json.charAt(index) != '[') {
            return false;
        }
        ++index;
        this.data = new ArrayList<JsonValue>();
        while (true) {
            JsonValue jsonValue = new JsonValue();
            if (!jsonValue.tryParse(json, index)) {
                break;
            }
            index = jsonValue.getNewOffset();
            index = this.skipWhitespaces(json, index);
            if (index == json.length()) {
                return false;
            }
            this.data.add(jsonValue);
            if (json.charAt(index) != ',') {
                break;
            }
            ++index;
        }
        index = this.skipWhitespaces(json, index);
        if (json.charAt(index) != ']') {
            return false;
        }
        this.setNewOffset(index + 1);
        return true;
    }

    @Override
    public String toString() {
        String json = "[";
        boolean first = true;
        for (JsonValue value : this.data) {
            if (first) {
                first = false;
            } else {
                json += ",";
            }
            json += value.toString();
        }
        json += "]";
        return json;
    }
    
    public List<JsonValue> getArray() {
        return this.data;
    }
    
}
