package com.devbaltasarq.jsson;

import java.io.BufferedWriter;
import java.io.Writer;

public class JsonWriter extends BufferedWriter {
    public JsonWriter(Writer wrt)
    {
        super( wrt );
    }
}
