package zhaohg.json;

public class JsonNumber extends JsonValue {
    
    private double data;
    
    private static final int STATE_BEGIN = 0;
    private static final int STATE_NEGATIVE = 1;
    private static final int STATE_ZERO = 2;
    private static final int STATE_NONZERO = 3;
    private static final int STATE_POINT = 4;
    private static final int STATE_FRACTION = 5;
    private static final int STATE_E = 6;
    private static final int STATE_E_SIGN = 7;
    private static final int STATE_E_EXP = 8;
    private static final int STATE_END = 9;
    
    public JsonNumber() {
        this.data = 0.0;
    }
    
    public JsonNumber(double value) {
        this.data = value;
    }
    
    public void setNumber(double value) {
        this.data = value;
    }

    @Override
    public boolean tryParse(String json, int offset) {
        int begin = this.skipWhitespaces(json, offset);
        int index = begin;
        int state = STATE_BEGIN;
        boolean finished = false;
        for (; index <= json.length() && !finished; ++index) {
            char c = ' ';
            if (index < json.length()) {
                c = json.charAt(index);
            }
            switch (state) {
            case STATE_BEGIN:
                if (c == '-') {
                    state = STATE_NEGATIVE;
                } else if (c == '0') {
                    state = STATE_ZERO;
                } else if ('1' <= c && c <= '9') {
                    state = STATE_NONZERO;
                } else {
                    finished = true;
                }
                break;
            case STATE_NEGATIVE:
                if (c == '0') {
                    state = STATE_ZERO;
                } else if ('1' <= c && c <= '9') {
                    state = STATE_NONZERO;
                } else {
                    finished = true;
                }
                break;
            case STATE_ZERO:
                if (c == '.') {
                    state = STATE_POINT;
                } else if (isE(c)) {
                    state = STATE_E;
                } else {
                    state = STATE_END;
                }
                break;
            case STATE_NONZERO:
                if (c == '.') {
                    state = STATE_POINT;
                } else if (isE(c)) {
                    state = STATE_E;
                } else if (isDigit(c)) {
                    state = STATE_NONZERO;
                } else {
                    state = STATE_END;
                }
                break;
            case STATE_POINT:
                if (isDigit(c)) {
                    state = STATE_FRACTION;
                } else {
                    finished = true;
                }
                break;
            case STATE_FRACTION:
                if (isE(c)) {
                    state = STATE_E;
                } else if (isDigit(c)) {
                    state = STATE_FRACTION;
                } else {
                    state = STATE_END;
                }
                break;
            case STATE_E:
                if (c == '+' || c == '-') {
                    state = STATE_E_SIGN;
                } else if (isDigit(c)) {
                    state = STATE_E_EXP;
                } else {
                    finished = true;
                }
                break;
            case STATE_E_SIGN:
                if (isDigit(c)) {
                    state = STATE_E_EXP;
                } else {
                    finished = true;
                }
                break;
            case STATE_E_EXP:
                if (isDigit(c)) {
                    state = STATE_E_EXP;
                } else {
                    state = STATE_END;
                }
                break;
            }
            if (state == STATE_END) {
                break;
            }
        }
        int end = index;
        if (state != STATE_END) {
            return false;
        }
        try {
            this.data = Double.parseDouble(json.substring(begin, end));
        } catch (NumberFormatException e) {
            return false;
        }
        this.setNewOffset(end);
        return true;
    }

    @Override
    public String toString() {
        String json = Double.toString(this.data);
        if (json.length() > 2) {
            if (json.substring(json.length() - 2).equals(".0")) {
                json = json.substring(0, json.length() - 2);
            }
        }
        return json;
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }
    
    private boolean isE(char c) {
        return c == 'E' || c == 'e';
    }
    
    public double getNumber() {
        return this.data;
    }

    public int getInteger() { return (int) this.data; }

}
