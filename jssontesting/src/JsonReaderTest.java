import com.devbaltasarq.jsson.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JsonReaderTest {
    @Test
    public void testEmptyObject()
    {
        String jsonObject = "{}";
        StringReader reader = new StringReader( jsonObject );

        try {
            JsonReader jsonReader = new JsonReader( reader );

            jsonReader.beginObject();
            while ( jsonReader.hasNext() ) {
                throw new IOException( "should not have contents" );
            }
            jsonReader.endObject();
        } catch(IOException exc)  {
            assertFalse( "reading empty object: " + exc.getMessage(), true );
        }
    }

    @Test
    public void testEmptyArray()
    {
        String jsonObject = "[]";
        StringReader reader = new StringReader( jsonObject );

        try {
            JsonReader jsonReader = new JsonReader( reader );

            jsonReader.beginArray();
            while ( jsonReader.hasNext() ) {
                throw new IOException( "should not have contents" );
            }
            jsonReader.endArray();
        } catch(IOException exc)  {
            assertFalse( "reading empty array: " + exc.getMessage(), true );
        }
    }

    @Test
    public void testSimpleObjectIdsNoQuotes()
    {
        int x = -1;
        int y = -1;
        String jsonObject = "{x:11,y:17}";
        StringReader reader = new StringReader( jsonObject );

        try {
            // Read
            JsonReader jsonReader = new JsonReader( reader );

            jsonReader.beginObject();
            while ( jsonReader.hasNext() ) {
                final String nextName = jsonReader.nextName();

                if ( nextName.equals( "x" ) ) {
                    x = jsonReader.nextInt();
                }
                else
                if ( nextName.equals( "y" ) ) {
                    y = jsonReader.nextInt();
                }
            }
            jsonReader.endObject();

            // Chk
            assertFalse( jsonReader.hasNext() );
            assertEquals( 11, x );
            assertEquals( 17, y );
        } catch(IOException exc)  {
            assertFalse( "reading simple object: " + exc.getMessage(), true );
        }
    }

    @Test
    public void testSimpleObjectIdsWithQuotes()
    {
        int x = -1;
        int y = -1;
        String jsonObject = "{\"x\":11,\"y\":17}";
        StringReader reader = new StringReader( jsonObject );

        try {
            // Read
            JsonReader jsonReader = new JsonReader( reader );

            jsonReader.beginObject();
            while ( jsonReader.hasNext() ) {
                final String nextName = jsonReader.nextName();

                if ( nextName.equals( "x" ) ) {
                    x = jsonReader.nextInt();
                }
                else
                if ( nextName.equals( "y" ) ) {
                    y = jsonReader.nextInt();
                }
            }
            jsonReader.endObject();

            // Chk
            assertFalse( jsonReader.hasNext() );
            assertEquals( 11, x );
            assertEquals( 17, y );
        } catch(IOException exc)  {
            assertFalse( "reading simple object: " + exc.getMessage(), true );
        }
    }
}
