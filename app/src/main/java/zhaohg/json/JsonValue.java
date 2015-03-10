package zhaohg.json;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class JsonValue extends JsonAbstract {
    
    public static final int TYPE_INVALID = 0;
    public static final int TYPE_STRING = 1;
    public static final int TYPE_NUMBER = 2;
    public static final int TYPE_OBJECT = 3;
    public static final int TYPE_ARRAY = 4;
    public static final int TYPE_BOOLEAN = 5;
    public static final int TYPE_NULL = 6;
    
    private JsonAbstract data;
    private int type;
    
    public JsonValue() {
        this.data = null;
        this.type = TYPE_NULL;
    }
    
    public JsonValue(String value) {
        this.setString(value);
    }
    
    public JsonValue(double value) {
        this.setNumber(value);
    }
    
    public JsonValue(SortedMap<JsonString, JsonValue> value) {
        this.setObject(value);
    }
    
    public JsonValue(Map<JsonString, JsonValue> value) {
        this.setObject(value);
    }
    
    public JsonValue(List<JsonValue> value) {
        this.setArray(value);
    }
    
    public JsonValue(boolean value) {
        this.setBoolean(value);
    }
    
    public void setString(String value) {
        this.data = new JsonString(value);
        this.type = TYPE_STRING;
    }
    
    public void setNumber(double value) {
        this.data = new JsonNumber(value);
        this.type = TYPE_NUMBER;
    }
    
    public void setObject(SortedMap<JsonString, JsonValue> value) {
        this.data = new JsonObject(value);
        this.type = TYPE_OBJECT;
    }
    
    public void setObject(Map<JsonString, JsonValue> value) {
        this.data = new JsonObject(value);
        this.type = TYPE_OBJECT;
    }
    
    public void setArray(List<JsonValue> value) {
        this.data = new JsonArray(value);
        this.type = TYPE_ARRAY;
    }
    
    public void setBoolean(boolean value) {
        this.data = new JsonBoolean(value);
        this.type = TYPE_BOOLEAN;
    }

    @Override
    public boolean tryParse(String json, int offset) {
        int index = this.skipWhitespaces(json, offset);
        if (index == json.length()) {
            return false;
        }
        this.data = new JsonString();
        if (this.data.tryParse(json, index)) {
            this.setNewOffset(this.data.getNewOffset());
            this.type = TYPE_STRING;
            return true;
        }
        this.data = new JsonNumber();
        if (this.data.tryParse(json, index)) {
            this.setNewOffset(this.data.getNewOffset());
            this.type = TYPE_NUMBER;
            return true;
        }
        this.data = new JsonObject();
        if (this.data.tryParse(json, index)) {
            this.setNewOffset(this.data.getNewOffset());
            this.type = TYPE_OBJECT;
            return true;
        }
        this.data = new JsonArray();
        if (this.data.tryParse(json, index)) {
            this.setNewOffset(this.data.getNewOffset());
            this.type = TYPE_ARRAY;
            return true;
        }
        this.data = new JsonBoolean();
        if (this.data.tryParse(json, index)) {
            this.setNewOffset(this.data.getNewOffset());
            this.type = TYPE_BOOLEAN;
            return true;
        }
        this.data = new JsonNull();
        if (this.data.tryParse(json, index)) {
            this.setNewOffset(this.data.getNewOffset());
            this.type = TYPE_NULL;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (this.data == null) {
            return "null";
        }
        return this.data.toString();
    }
    
    public int getType() {
        return this.type;
    }
    public String getString() {
        return ((JsonString)this.data).getString();
    }
    
    public double getNumber() {
        return ((JsonNumber)this.data).getNumber();
    }
    
    public Map<JsonString, JsonValue> getObject() {
        return ((JsonObject)this.data).getObject();
    }
    
    public List<JsonValue> getArray() {
        return ((JsonArray)this.data).getArray();
    }
    
    public boolean getBoolean() {
        return ((JsonBoolean)this.data).getBoolean();
    }
    
    public boolean isNull() {
        return this.getType() == TYPE_NULL;
    }
    

}
