package zhaohg.json;

public abstract class JsonAbstract {
    
    private int newOffset;
    
    public boolean tryParse(String json) {
        return this.tryParse(json, 0);
    }

    public abstract boolean tryParse(String json, int offset);
    public abstract String toString();
    
    public boolean equals(Object obj) {
        return this.toString() == obj.toString();
    }
    
    protected void setNewOffset(int offset) {
        this.newOffset = offset;
    }
    
    public int getNewOffset() {
        return this.newOffset;
    }
    
    public int skipWhitespaces(String json, int offset) {
        int index = offset;
        for (; index < json.length(); ++index) {
            if (!Character.isWhitespace(json.charAt(index))) {
                break;
            }
        }
        return index;
    }

}
