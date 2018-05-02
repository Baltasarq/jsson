import com.devbaltasarq.jsson.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JsonWriterTest {

    @Test
    public void testWritingEmptyObject()
    {
        final String EmptyObject = "{}";
        final StringWriter writer = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter( writer );

        try {
            // Write
            jsonWriter.beginObject()
                    .endObject();
            jsonWriter.close();
            writer.close();

            // Chk
            assertEquals( EmptyObject, writer.toString() );
        } catch(IOException exc)
        {
            assertFalse( "writing empty object: " + exc.getMessage(), true );
        }
    }

    @Test
    public void testWritingEmptyArray()
    {
        final String EmptyArray = "[]";
        final StringWriter writer = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter( writer );

        try {
            // Write
            jsonWriter.beginArray()
                    .endArray();
            jsonWriter.close();
            writer.close();

            // Chk
            assertEquals( EmptyArray, writer.toString() );
        } catch(IOException exc)
        {
            assertFalse( "writing empty array: " + exc.getMessage(), true );
        }
    }

    @Test
    public void testWritingPointObject()
    {
        final String PointObject = "{\"x\":5,\"y\":6}";
        final StringWriter writer = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter( writer );

        try {
            // Write
            jsonWriter.beginObject()
                    .name( "x" ).value( 5 )
                    .name( "y" ).value( 6 )
                    .endObject();
            jsonWriter.close();
            writer.close();

            // Chk
            assertEquals( PointObject, writer.toString() );
        } catch(IOException exc)
        {
            assertFalse( "writing point object: " + exc.getMessage(), true );
        }
    }

    @Test
    public void testMultipleTypesObject()
    {
        final String PointObject = "{\"a\":true,\"b\":false,\"pi\":3.14,\"one\":1,\"author\":\"baltasar\"}";
        final StringWriter writer = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter( writer );

        try {
            // Write
            jsonWriter.beginObject()
                    .name( "a" ).value( true )
                    .name( "b" ).value( false )
                    .name( "pi" ).value( 3.14 )
                    .name( "one" ).value( 1 )
                    .name( "author" ).value( "baltasar" )
                    .endObject();
            jsonWriter.close();
            writer.close();

            // Chk
            assertEquals( PointObject, writer.toString() );
        } catch(IOException exc)
        {
            assertFalse( "writing point object: " + exc.getMessage(), true );
        }
    }
}
