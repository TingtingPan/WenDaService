package com.example.wenDaService.async;

public enum EventType {
    LIKE(0),
    COMMENT(1),
    lOGIN(2),
    MAIL(3);
    private int value;
    EventType(int value){this.value=value;}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
