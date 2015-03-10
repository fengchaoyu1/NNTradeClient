package zhaohg.json;

import java.lang.Comparable;

public class JsonString extends JsonValue implements Comparable<Object> {
    
    private String data;
    
    public JsonString() {
        this.data = "";
    }
    
    public JsonString(String value) {
        this.data = value;
    }
    
    public void setString(String value) {
        this.data = value;
    }

    @Override
    public boolean tryParse(String json, int offset) {
        int index = this.skipWhitespaces(json, offset);
        if (index == json.length() || json.charAt(index) != '"') {
            return false;
        }
        this.data = "";
        boolean translated = false;
        for (++index; index < json.length(); ++index) {
            char c = json.charAt(index);
            if (translated) {
                switch (c) {
                case '"':
                    this.data += '"';
                    break;
                case '\\':
                    this.data += '\\';
                    break;
                case '/':
                    this.data += '/';
                    break;
                case 'b':
                    this.data += '\b';
                    break;
                case 'f':
                    this.data += '\f';
                    break;
                case 'n':
                    this.data += '\n';
                    break;
                case 'r':
                    this.data += '\r';
                    break;
                case 't':
                    this.data += '\t';
                    break;
                case 'u':
                    if (index + 4 >= json.length()) {
                        return false;
                    }
                    int point = 0;
                    for (int i = 1; i <= 4; ++i) {
                        char dex = json.charAt(index + i);
                        point <<= 4;
                        if ('0' <= dex && dex <= '9') {
                            point += dex - '0';
                        } else if ('a' <= dex && dex <= 'f') {
                            point += dex - 'a' + 10;
                        } else if ('A' <= dex && dex <= 'F') {
                            point += dex - 'A' + 10;
                        } else {
                            return false;
                        }
                    }
                    index += 4;
                    this.data += Character.toString((char)point);
                    break;
                }
                translated = false;
            } else {
                if (c == '\\') {
                    translated = true;
                } else if (c == '"') {
                    this.setNewOffset(index + 1);
                    return true;
                } else {
                    this.data += c;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String json = "\"";
        for (int i = 0; i < data.length(); ++i) {
            if (data.charAt(i) == '"') {
                json += '\\';
            }
            json += data.charAt(i);
        }
        json += "\"";
        return json;
    }

    public int compareTo(Object obj) {
        return this.data.compareTo(((JsonString)obj).data);
    }
    
    public String getString() {
        return this.data;
    }
}
