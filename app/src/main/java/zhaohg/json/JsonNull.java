package zhaohg.json;

public class JsonNull extends JsonValue {
    
    public JsonNull() {
    }

    @Override
    public boolean tryParse(String json, int offset) {
        int index = this.skipWhitespaces(json, offset);
        if (index + 4 > json.length()) {
            return false;
        }
        if (json.charAt(index) == 'n') {
            if (json.charAt(index + 1) == 'u') {
                if (json.charAt(index + 2) == 'l') {
                    if (json.charAt(index + 3) == 'l') {
                        this.setNewOffset(index + 4);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "null";
    }

}
