package com.dev.voltsoft.lib.constatns;

import java.util.ArrayList;

public enum ValueType {

    STRING(String.class),

    INTEGER(int.class),

    DOUBLE(double.class),

    FLOAT(float.class),

    BOOLEAN(boolean.class),

    LIST(ArrayList.class);

    private Class<?> meValueType;

    ValueType(Class<?> type) {
        meValueType = type;
    }

    public boolean isEqualType(Class<?> parameter)
    {
        return meValueType.equals(parameter);
    }
}
