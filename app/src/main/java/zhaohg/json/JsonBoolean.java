package zhaohg.json;

public class JsonBoolean extends JsonValue {
    
    private Boolean data;
    
    public JsonBoolean() {
        this.data = false;
    }
    
    public JsonBoolean(boolean value) {
        this.data = value;
    }
    
    public void setBoolean(boolean value) {
        this.data = value;
    }

    @Override
    public boolean tryParse(String json, int offset) {
        int index = this.skipWhitespaces(json, offset);
        if (index + 4 > json.length()) {
            return false;
        }
        if (json.charAt(index) == 't') {
            if (json.charAt(index + 1) == 'r') {
                if (json.charAt(index + 2) == 'u') {
                    if (json.charAt(index + 3) == 'e') {
                        this.data = true;
                        this.setNewOffset(index + 4);
                        return true;
                    }
                }
            }
        }
        if (index + 5 > json.length()) {
            return false;
        }
        if (json.charAt(index) == 'f') {
            if (json.charAt(index + 1) == 'a') {
                if (json.charAt(index + 2) == 'l') {
                    if (json.charAt(index + 3) == 's') {
                        if (json.charAt(index + 4) == 'e') {
                            this.data = false;
                            this.setNewOffset(index + 5);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.data.toString();
    }
    
    public boolean getBoolean() {
        return this.data;
    }

}
