import com.devbaltasarq.jsson.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

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
    public void testSimplestObjectId()
    {
        String author = "";
        String jsonObject = "{'author':'baltasarq'}";
        StringReader reader = new StringReader( jsonObject );

        try {
            // Read
            JsonReader jsonReader = new JsonReader( reader );

            jsonReader.beginObject();
            while ( jsonReader.hasNext() ) {
                final String nextName = jsonReader.nextName();

                if ( nextName.equals( "author" ) ) {
                    author = jsonReader.nextString();
                }
            }
            jsonReader.endObject();

            // Chk
            assertFalse( jsonReader.hasNext() );
            assertEquals( author, "baltasarq" );
        } catch(IOException exc)  {
            assertFalse( "reading simplest object: " + exc.getMessage(), true );
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
            assertFalse( "reading simple object without quotes for names: " + exc.getMessage(), true );
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
            assertFalse( "reading simple object with quotes for names: " + exc.getMessage(), true );
        }
    }

    @Test
    public void testSimpleArray()
    {
        int[] numbers = new int[]{ 1, 2, 3 };
        ArrayList<Integer> read_numbers = new ArrayList<>();
        StringBuilder jsonObject = new StringBuilder( "[" );

        // Prepare
        String sep = "";
        for(int i = 0; i < numbers.length; ++i) {
            jsonObject.append( sep );
            jsonObject.append( Integer.toString( i + 1 ) );
            sep = ",";
        }
        jsonObject.append( ']' );

        // Read
        try {
            // Read
            final StringReader reader = new StringReader( jsonObject.toString() );
            JsonReader jsonReader = new JsonReader( reader );

            jsonReader.beginArray();
            while ( jsonReader.hasNext() ) {
                read_numbers.add( jsonReader.nextInt() );
            }
            jsonReader.endArray();

            // Chk
            assertFalse( jsonReader.hasNext() );
            assertEquals( numbers.length, read_numbers.size() );
            for(int i = 0; i < read_numbers.size(); ++i) {
                assertEquals( i + 1, (int) read_numbers.get( i ) );
            }
        } catch(IOException exc)  {
            assertFalse( "reading simple array: " + exc.getMessage(), true );
        }
    }

    @Test
    public void testComplexObject()
    {
        String firstName = "";
        String lastName = "";
        int age = -1;
        String streetAddress = "";
        String city = "";
        String state = "";
        String postalCode = "";
        String phoneType1 = "";
        String phoneType2 = "";
        String phoneNumber1 = "";
        String phoneNumber2 = "";
        String gender = "";
        String jsonObject = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Smith\",\n" +
                "  \"age\": 25,\n" +
                "  \"address\": {\n" +
                "    \"streetAddress\": \"21 2nd Street\",\n" +
                "    \"city\": \"New York\",\n" +
                "    \"state\": \"NY\",\n" +
                "    \"postalCode\": \"10021\"\n" +
                "  },\n" +
                "  \"phoneNumber\": [\n" +
                "    {\n" +
                "      \"type\": \"home\",\n" +
                "      \"number\": \"212 555-1234\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"fax\",\n" +
                "      \"number\": \"646 555-4567\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"gender\": {\n" +
                "    \"type\": \"male\"\n" +
                "  }\n" +
                "}";
        StringReader reader = new StringReader( jsonObject );

        try {
            // Read
            JsonReader jsonReader = new JsonReader( reader );

            jsonReader.beginObject();
            while ( jsonReader.hasNext() ) {
                String nextName = jsonReader.nextName();

                if ( nextName.equals( "firstName" ) ) {
                    firstName = jsonReader.nextString();
                }
                else
                if ( nextName.equals( "lastName" ) ) {
                    lastName = jsonReader.nextString();
                }
                else
                if ( nextName.equals( "address" ) ) {
                    jsonReader.beginObject();

                    while( jsonReader.hasNext() ) {
                        nextName = jsonReader.nextName();

                        if ( nextName.equals( "streetAddress" ) ) {
                            streetAddress = jsonReader.nextString();
                        }
                        else
                        if ( nextName.equals( "city" ) ) {
                            city = jsonReader.nextString();
                        }
                        else
                        if ( nextName.equals( "state" ) ) {
                            state = jsonReader.nextString();
                        }
                        else
                        if ( nextName.equals( "postalCode" ) ) {
                            postalCode = jsonReader.nextString();
                        }
                    }
                    jsonReader.endObject();
                }
                else
                if ( nextName.equals( "phoneNumber" ) ) {
                    jsonReader.beginArray();

                    while( jsonReader.hasNext() ) {
                        jsonReader.beginObject();

                        while( jsonReader.hasNext() ) {
                            nextName = jsonReader.nextName();

                            if ( nextName.equals( "type" ) ) {
                                if ( phoneType1.isEmpty() ) {
                                    phoneType1 = jsonReader.nextString();
                                } else {
                                    phoneType2 = jsonReader.nextString();
                                }
                            }
                            else
                            if ( nextName.equals( "number" ) ) {
                                if ( phoneNumber1.isEmpty() ) {
                                    phoneNumber1 = jsonReader.nextString();
                                } else {
                                    phoneNumber2 = jsonReader.nextString();
                                }
                            }
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                }
                else
                if ( nextName.equals( "age" ) ) {
                    age = jsonReader.nextInt();
                }
                else
                if ( nextName.equals( "gender" ) ) {
                    jsonReader.beginObject();
                    if ( jsonReader.nextName().equals( "type" ) ) {
                        gender = jsonReader.nextString();
                    } else {
                        throw new IOException( "missing unique 'type' in gender" );
                    }
                    jsonReader.endObject();
                }
            }
            jsonReader.endObject();

            // Chk
            assertFalse( jsonReader.hasNext() );
            assertEquals( "John", firstName );
            assertEquals( "Smith", lastName );
            assertEquals( "21 2nd Street", streetAddress );
            assertEquals( 25, age );
            assertEquals( "male", gender );
            assertEquals( "New York", city );
            assertEquals( "NY", state );
            assertEquals( "10021", postalCode );
            assertEquals( "home", phoneType1 );
            assertEquals( "fax", phoneType2 );
            assertEquals( "212 555-1234", phoneNumber1 );
            assertEquals( "646 555-4567", phoneNumber2 );
        } catch(IOException exc)  {
            assertFalse( "reading complex object: " + exc.getMessage(), true );
        }
    }
}
