package com.dev.voltsoft.lib.model;

import android.database.Cursor;
import com.dev.voltsoft.lib.constatns.ValueType;

import java.lang.reflect.Field;
import java.util.*;

public abstract class BaseModel extends Observable
{

    public BaseModel()
    {

    }

    public LinkedList<Field> fieldList()
    {
        LinkedList<Field> linkedList = new LinkedList<>();
        linkedList.addAll(Arrays.asList(getClass().getDeclaredFields()));

        if (getClass().getSuperclass() != null)
        {
            linkedList.addAll(Arrays.asList(getClass().getSuperclass().getDeclaredFields()));
        }

        return linkedList;
    }

    public Object fieldValue(Field field)
    {
        return fieldValue(field.getName());
    }

    @SuppressWarnings("unchecked")
    public Object fieldValue(String fieldName)
    {

        try
        {
            Object resultObject = null;

            for (Field field : fieldList())
            {

                if (!field.isSynthetic())
                {
                    field.setAccessible(true);

                    if (field.getName().equalsIgnoreCase(fieldName))
                    {

                        if (ValueType.LIST.isEqualType(field.getType()))
                        {
                            boolean bNotFist = false;
                            StringBuilder convertListData = new StringBuilder();

                            ArrayList<String> arrayList =
                                    (ArrayList<String>) field.get(this);
                            for (String str : arrayList) {
                                if (bNotFist) {
                                    convertListData.append("&");
                                }
                                convertListData.append(str);
                                bNotFist = true;
                            }
                            resultObject = convertListData;

                        }
                        else if (ValueType.STRING.isEqualType(field.getType())
                                || ValueType.INTEGER.isEqualType(field.getType())
                                || ValueType.DOUBLE.isEqualType(field.getType())
                                || ValueType.FLOAT.isEqualType(field.getType())
                                || ValueType.BOOLEAN.isEqualType(field.getType())) {

                            resultObject = field.get(this);
                        }
                        break;
                    }
                }
            }
            return resultObject;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public void matchingCursor(Cursor cursor)
    {
        for (Field field : fieldList())
        {
            String fieldName = field.getName();

            int index = cursor.getColumnIndex(fieldName);

            if (index >= 0 && !field.isSynthetic() && !"serialVersionUID".equalsIgnoreCase(field.getName()))
            {
                String columnData = cursor.getString(index);

                try
                {
                    if (columnData != null)
                    {
                        field.setAccessible(true);

                        matchingCursor(field , columnData);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void matchingCursor(Field field , String columnData) throws Exception
    {
        Class<?> fieldType = field.getType();

        for (ValueType valueType : ValueType.values())
        {
            if (valueType.isEqualType(fieldType))
            {
                matchingCursor(field , valueType , columnData);

                return;
            }
        }
    }

    private void matchingCursor(Field field , ValueType valueType , String columnData) throws Exception
    {
        switch (valueType)
        {
            case STRING:
                field.set(this , columnData);
                break;

            case DOUBLE:
                Double aDouble = Double.parseDouble(columnData);
                field.set(this , aDouble);
                break;

            case INTEGER:
                Integer integer = Integer.parseInt(columnData);
                field.set(this , integer);
                break;

            case FLOAT:
                Float aFloat = Float.parseFloat(columnData);
                field.set(this , aFloat);
                break;

            case BOOLEAN:
                boolean aBoolean = Boolean.parseBoolean(columnData);
                field.setBoolean(this , aBoolean);
                break;

            case LIST:
                ArrayList<String> stringArrayList = new ArrayList<>();

                String[] selections = columnData.split("&");

                Collections.addAll(stringArrayList, selections);
                Collections.shuffle(stringArrayList);

                field.set(this , stringArrayList);
                break;
        }
    }
}
