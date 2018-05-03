import com.devbaltasarq.jsson.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
            fail( "writing empty object: " + exc.getMessage() );
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
            fail( "writing empty array: " + exc.getMessage() );
        }
    }

    @Test
    public void testWritingSimpleArray()
    {
        final String EmptyArray = "[1,2,3]";
        final StringWriter writer = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter( writer );

        try {
            // Write
            jsonWriter.beginArray()
                        .value( 1 )
                        .value( 2 )
                        .value( 3 )
                    .endArray();
            jsonWriter.close();
            writer.close();

            // Chk
            assertEquals( EmptyArray, writer.toString() );
        } catch(IOException exc)
        {
            fail( "writing empty array: " + exc.getMessage() );
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
            fail( "writing point object: " + exc.getMessage() );
        }
    }

    @Test
    public void testWritingMultipleTypesObject()
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
            fail( "writing multiple types object: " + exc.getMessage() );
        }
    }

    @Test
    public void testWritingComposedObject()
    {
        final String LineObject = "{\"begin\":{\"x\":5,\"y\":6},\"end\":{\"x\":11,\"y\":12}}";
        final StringWriter writer = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter( writer );

        try {
            // Write
            jsonWriter.beginObject()
                    .name( "begin" ).beginObject()
                        .name("x").value( 5 )
                        .name("y").value( 6 )
                    .endObject()
                    .name( "end" ).beginObject()
                        .name("x").value( 11 )
                        .name("y").value( 12 )
                    .endObject()
                .endObject();
            jsonWriter.close();
            writer.close();

            // Chk
            assertEquals( LineObject, writer.toString() );
        } catch(IOException exc)
        {
            fail( "writing line object: " + exc.getMessage() );
        }
    }

    @Test
    public void testComplexObject()
    {
        String jsonObject = "{\"firstName\":\"John\"" +
                ",\"lastName\":\"Smith\"" +
                ",\"age\":25" +
                ",\"address\":{" +
                    "\"streetAddress\":\"21 2nd Street\"" +
                    ",\"city\":\"New York\"" +
                    ",\"state\":\"NY\"" +
                    ",\"postalCode\":\"10021\"" +
                "}" +
                ",\"phoneNumber\":[" +
                    "{\"type\":\"home\"" +
                    ",\"number\":\"212 555-1234\"}" +
                    ",{\"type\":\"fax\"" +
                    ",\"number\":\"646 555-4567\"}" +
                "],\"gender\":{\"type\":\"male\"}}";
        final StringWriter writer = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter( writer );

        try {
            // Write
            jsonWriter.beginObject()
                    .name( "firstName" ).value( "John" )
                    .name( "lastName" ).value( "Smith" )
                    .name( "age" ).value( 25 )
                    .name( "address" ).beginObject()
                        .name( "streetAddress" ).value( "21 2nd Street" )
                        .name( "city" ).value( "New York" )
                        .name( "state" ).value( "NY" )
                        .name( "postalCode" ).value( "10021" )
                    .endObject()
                    .name( "phoneNumber" ).beginArray()
                        .beginObject()
                            .name( "type" ).value( "home" )
                            .name( "number" ).value( "212 555-1234" )
                        .endObject()
                        .beginObject()
                            .name( "type" ).value( "fax" )
                            .name( "number" ).value( "646 555-4567" )
                        .endObject()
                    .endArray()
                    .name( "gender" ).beginObject()
                        .name( "type" ).value( "male" )
                    .endObject()
                .endObject();
            jsonWriter.close();
            writer.close();

            // Chk
            assertEquals( jsonObject, writer.toString() );
        } catch(IOException exc)
        {
            fail( "writing complex json object: " + exc.getMessage() );
        }
    }
}
