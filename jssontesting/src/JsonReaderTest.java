import com.devbaltasarq.jsson.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

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
}
