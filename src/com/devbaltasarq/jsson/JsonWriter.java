// jsson (c) 2018 Baltasar MIT License <baltasarq@gmail.com>

package com.devbaltasarq.jsson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class JsonWriter extends BufferedWriter {
    public JsonWriter(Writer wrt)
    {
        super( wrt );

        this.beginning = false;
    }

    /** Writes the name part of a name/value pair.
      * @param name The name to write, escaped.
      * @return The writer itself.
      * @throws IOException if something goes wrong writing.
      */
    public JsonWriter name(String name) throws IOException
    {
        // Separator
        if ( !this.beginning ) {
            this.write( Util.ENTITY_SEPARATOR );
        } else {
            beginning = false;
        }

        // Value
        this.write( Util.QUOTES );
        this.write( name );
        this.write( Util.QUOTES );
        this.write( Util.NAME_SEPARATOR );
        return this;
    }

    /** Writes a boolean value.
      * @param value The value to write.
      * @return The JsonWriter itself.
      * @throws IOException if something goes wrong writing.
      */
    public JsonWriter value(Boolean value) throws IOException
    {
        this.write( Boolean.toString( value ) );
        return this;
    }

    /** Writes an integer number.
     * @param value The value to write.
     * @return The JsonWriter itself.
     * @throws IOException if something goes wrong writing.
     */
    public JsonWriter value(int value) throws IOException
    {
        this.write( Integer.toString( value ) );
        return this;
    }

    /** Writes a real number.
     * @param value The value to write.
     * @return The JsonWriter itself.
     * @throws IOException if something goes wrong writing.
     */
    public JsonWriter value(double value) throws IOException
    {
        this.write( Double.toString( value ) );
        return this;
    }

    /** Writes a string.
     * @param value The value to write.
     * @return The JsonWriter itself.
     * @throws IOException if something goes wrong writing.
     */
    public JsonWriter value(String value) throws IOException
    {
        this.write( Util.QUOTES );
        this.write( value );
        this.write( Util.QUOTES );
        return this;
    }

    /** Writes a null value.
     * @return The JsonWriter itself.
     * @throws IOException if something goes wrong writing.
     */
    public JsonWriter nullValue() throws IOException
    {
        this.write( Util.NULL_ID );
        return this;
    }

    /** Writes the begin mark for an object.
      * @return The JsonWriter itself.
      * @throws IOException if something goes wrong writing.
      */
    public JsonWriter beginObject() throws IOException
    {
        this.beginning = true;
        this.write( Util.OPEN_OBJECT_DELIMITER );
        return this;
    }

    /** Writes the end mark for an object.
     * @return The JsonWriter itself.
     * @throws IOException if something goes wrong writing.
     */
    public JsonWriter endObject() throws IOException
    {
        this.write( Util.END_OBJECT_DELIMITER );
        return this;
    }
    /** Writes the begin mark for an array.
     * @return The JsonWriter itself.
     * @throws IOException if something goes wrong writing.
     */
    public JsonWriter beginArray() throws IOException
    {
        this.beginning = true;
        this.write( Util.OPEN_ARRAY_DELIMITER );
        return this;
    }

    /** Writes the end mark for an array.
     * @return The JsonWriter itself.
     * @throws IOException if something goes wrong writing.
     */
    public JsonWriter endArray() throws IOException
    {
        this.write( Util.END_ARRAY_DELIMITER );
        return this;
    }

    private boolean beginning;
}
