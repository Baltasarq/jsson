package com.devbaltasarq.jsson;

import java.io.BufferedWriter;
import java.io.Writer;

public class JsonWriter extends BufferedWriter {
    private final static String NULL_ID = "null";
    private final static String OPEN_OBJECT = "{";
    private final static String END_OBJECT = "}";
    private final static String OPEN_ARRAY = "[";
    private final static String END_ARRAY = "]";
    private final static String NAME_SEPARATOR = ":";
    private final static String ENTITY_SEPARATOR = ",";

    public JsonWriter(Writer wrt)
    {
        super( wrt );
    }


}
