// jsson (c) Baltasar MIT License 2018 <baltasarq@gmail.com>

package com.devbaltasarq.jsson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a new JSON reader.
 * @author baltasarq
 */
public class JsonReader extends BufferedReader {
    private static final String TOKEN_VALID_CHARS = "_0123456789abcdefghijklmnopqrstuvvwxyz";
    public enum TokenType { ERROR, END, UNESCAPED_ID, STRING, NUMBER,
        OPEN_OBJECT, CLOSE_OBJECT,
        OPEN_ARRAY, CLOSE_ARRAY }


    /** Creates a new JsonReader, given an input stream.
     * @param input an input stream to read from.
     */
    public JsonReader(Reader input)
    {
        super( input );

        this.reader = new PushbackReader( this );
    }

    /** Skips all spaces, tabs, newlines, commas... */
    public void skipSpaces()
    {
        try {
            int ch = this.reader.read();

            while( ch != -1 ) {
                if ( !Character.isSpaceChar( ch )
                  && ch != ',' )
                {
                    this.reader.unread( ch );
                    break;
                }

                ch = this.reader.read();
            }
        } catch(IOException exc)
        {
            Logger.getGlobal().log( Level.WARNING, "attempted to skip spaces in a closed stream" );
        }

        return;
    }

    /** @return whether there is more input or not. */
    public boolean hasNext()
    {
        boolean toret = true;

        this.skipSpaces();

        try {
            final int ch = this.reader.read();
            this.reader.unread( ch );

            if ( this.nextTokenType() == TokenType.CLOSE_ARRAY
              || this.nextTokenType() == TokenType.CLOSE_OBJECT )
            {
                toret = false;
            }
        } catch(IOException exc)
        {
            toret = false;
        }

        return toret;
    }

    public TokenType nextTokenType()
    {
        TokenType toret = TokenType.ERROR;

        try {
            // Go to the first important data
            this.skipSpaces();
            final int ch = this.reader.read();

            // Consider next char
            this.reader.unread( ch );
            if ( ch != -1 ) {
                if ( Character.isDigit( ch )
                  || ch == '.' )
                {
                    toret = TokenType.NUMBER;
                }
                else
                if ( Character.isLetter( ch )
                  || ch == '_' )
                {
                    toret = TokenType.UNESCAPED_ID;
                }
                else
                if ( ch == '\''
                  || ch == '"' )
                {
                    toret = TokenType.STRING;
                }
                else
                if ( ch == '{' )
                {
                    toret = TokenType.OPEN_OBJECT;
                }
                else
                if ( ch == '[' )
                {
                    toret = TokenType.OPEN_ARRAY;
                }
                else
                if ( ch == '}' )
                {
                    toret = TokenType.CLOSE_OBJECT;
                }
                else
                if ( ch == ']' )
                {
                    toret = TokenType.CLOSE_ARRAY;
                }
            } else {
                toret = TokenType.END;
            }
        } catch(IOException exc)
        {
            Logger.getGlobal().log( Level.WARNING, "nextTokenType(): " + exc.getMessage() );
            toret = TokenType.ERROR;
        }

        return toret;
    }

    private static boolean isValidCharForToken(int c)
    {
        return TOKEN_VALID_CHARS.indexOf( Character.toLowerCase( c ) ) >= 0;
    }

    /** Gets the next identifier.
     * @return the identifier, as a string.
     * @throws IOException if reading goes wrong.
     */
    public String getToken() throws IOException
    {
        StringBuffer token = new StringBuffer();
        int ch = this.reader.read();

        // Collect the id
        while ( ch != -1
             && isValidCharForToken( ch ) )
        {
            if ( ch == '\\' ) {
                this.reader.unread( ch );
                ch = this.parseSpecialChar();
            }

            token.append( ch );
            ch = this.reader.read();
        }

        // Push back the last char read, if possible
        if ( ch != -1 ) {
            this.reader.unread( ch );
        }

        return token.toString();
    }

    public void beginObject() throws IOException
    {
        this.skipSpaces();
        this.match( '{' );
        this.skipSpaces();
    }

    public void endObject() throws IOException
    {
        this.skipSpaces();
        this.match( '}' );
        this.skipSpaces();
    }

    public void beginArray() throws IOException
    {
        this.skipSpaces();
        this.match( '[' );
        this.skipSpaces();
    }

    public void endArray() throws IOException
    {
        this.skipSpaces();
        this.match( ']' );
        this.skipSpaces();
    }

    public String nextFloat() throws IOException
    {
        throw new IOException( "not implemented yet" );
    }

    public String nextInt() throws IOException
    {
        throw new IOException( "not implemented yet" );
    }

    /** If the given char is not found, throws an exception.
     * @param ch the expected char.
     * @throws IOException if reading goes wrong,
     *                     or the read char is not the expected.
     */
    public void match(char ch) throws IOException
    {
        final int readChar = this.reader.read();

        if ( readChar == -1
          || readChar != ch )
        {
            throw new IOException( "expected: '" + readChar
                                    + "', but found: '" + ch + "'" );
        }

        return;
    }

    /** @return the next name in the JSON stream. */
    public String nextName() throws IOException
    {
        String toret = null;

        // Read next char
        this.skipSpaces();
        int ch = this.reader.read();
        this.reader.unread( ch );

        // Check for quotes or double-quotes
        if ( ch == '"'
          || ch == '\'' )
        {
            toret = this.nextString();
        } else {
            toret = this.getToken();
        }

        return toret;
    }

    /** @return the next string, no matter of being enclosed
     *          in single or double quotes. */
    public String nextString() throws IOException
    {
        String toret = null;
        int ch;
        int quotes = -1;

        // Advance
        this.skipSpaces();
        ch = this.reader.read();

        // Check for quotes or double-quotes
        if ( ch == '"'
          || ch == '\'' )
        {
            quotes = ch;
        } else {
            throw new IOException( "expected quotes: ' or double quotes: \"" );
        }

        // Read the id inside
        toret = this.getToken();

        // Read quotes if they were present
        if ( quotes != -1 ) {
            this.match( (char) quotes );
        }

        return toret;
    }

    /** Closes the reader. */
    @Override
    public void close()
    {
        try {
            this.reader.close();
            super.close();
        } catch(IOException exc) {
            Logger.getGlobal().log( Level.SEVERE, "error closing input stream" );
        }

        return;
    }

    private char parseSpecialChar() throws IOException
    {
        int ch = this.reader.read();

        if ( ch == '\\' ) {
            ch = this.reader.read();

            switch (ch) {
                case '\\':
                    ch = '\\';
                    break;
                case '"':
                    ch = '"';
                    break;
                case '\'':
                    ch = '\'';
                    break;
                case 'n':
                    ch = '\n';
                    break;
                case 't':
                    ch = '\t';
                    break;
                default:
                    throw new IOException( "invalid special char: \\" + Character.toString( (char) ch ) );
            }
        } else {
            throw new IOException( "not a special char" );
        }

        return (char) ch;
    }

    private PushbackReader reader;
}
