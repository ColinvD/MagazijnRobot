public class DatabaseValue {
    private String colomn;
    private Object value;

    public DatabaseValue(String colomn, Object value) {
        this.colomn = colomn;
        this.value = value;
    }

    public String getColomn() {
        return colomn;
    }

    public void setColomn(String colomn) {
        this.colomn = colomn;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
