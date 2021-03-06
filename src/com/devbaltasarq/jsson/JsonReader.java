// jsson (c) 2018 Baltasar MIT License <baltasarq@gmail.com>

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
    public enum TokenType {
        ERROR, END, UNESCAPED_ID, STRING, NUMBER, BOOLEAN, NULL,
        OPEN_OBJECT, CLOSE_OBJECT,
        OPEN_ARRAY, CLOSE_ARRAY }


    /** Creates a new JsonReader, given an input stream.
     * @param input an input stream to read from.
     */
    public JsonReader(Reader input)
    {
        super( input );

        this.reader = new PushbackReader( this, 256 );
    }

    /** Skips all spaces, tabs, newlines, commas... */
    public void skipSpaces()
    {
        try {
            int ch = this.reader.read();

            while( ch != -1 ) {
                if ( !Character.isSpaceChar( ch )
                  && Util.EXTRA_SEPARATORS.indexOf( ch ) < 0 )
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

            if ( ch == -1
              || this.nextTokenType() == TokenType.CLOSE_ARRAY
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

    /** @return the type of the token ahead. */
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
                  || ch == '+'
                  || ch == '-'
                  || ch == '.' )
                {
                    toret = TokenType.NUMBER;
                }
                else
                if ( Character.isLetter( ch )
                  || ch == '_' )
                {
                    if ( this.isNullAhead() ) {
                        toret = TokenType.NULL;
                    }
                    else
                    if ( this.isBooleanAhead() ) {
                        toret = TokenType.BOOLEAN;
                    }
                    else {
                        toret = TokenType.UNESCAPED_ID;
                    }
                }
                else
                if ( ch == '\''
                  || ch == '"' )
                {
                    toret = TokenType.STRING;
                }
                else
                if ( ch == Util.OPEN_OBJECT_DELIMITER )
                {
                    toret = TokenType.OPEN_OBJECT;
                }
                else
                if ( ch == Util.OPEN_ARRAY_DELIMITER )
                {
                    toret = TokenType.OPEN_ARRAY;
                }
                else
                if ( ch == Util.END_OBJECT_DELIMITER )
                {
                    toret = TokenType.CLOSE_OBJECT;
                }
                else
                if ( ch == Util.END_ARRAY_DELIMITER )
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

    /** @return true when the next token is null, false otherwise. */
    public boolean isBooleanAhead() throws IOException
    {
        this.skipSpaces();
        final String token = this.getToken();

        this.reader.unread( token.toCharArray() );
        return token.equals( Boolean.toString( true ) )
            || token.equals( Boolean.toString( false ) );
    }

    /** @return true when the next token is null, false otherwise. */
    public boolean isNullAhead() throws IOException
    {
        this.skipSpaces();
        final String token = this.getToken();

        this.reader.unread( token.toCharArray() );
        return token.equals( Util.NULL_ID );
    }

    /** Gets the next identifier.
     * @return the identifier, as a string.
     * @throws IOException if reading goes wrong.
     */
    public String getToken() throws IOException
    {
        final StringBuilder token = new StringBuilder();

        this.skipSpaces();
        int ch = this.reader.read();

        // Collect the id
        while ( ch != -1
             && isValidCharForToken( ch ) )
        {
            token.append( (char) ch );
            ch = this.reader.read();
        }

        this.reader.unread( ch );
        return token.toString();
    }

    public void beginObject() throws IOException
    {
        this.skipSpaces();
        this.match( Util.OPEN_OBJECT_DELIMITER );
        this.skipSpaces();
    }

    public void endObject() throws IOException
    {
        this.skipSpaces();
        this.match( Util.END_OBJECT_DELIMITER );
        this.skipSpaces();
    }

    public void beginArray() throws IOException
    {
        this.skipSpaces();
        this.match( Util.OPEN_ARRAY_DELIMITER );
        this.skipSpaces();
    }

    public void endArray() throws IOException
    {
        this.skipSpaces();
        this.match( Util.END_ARRAY_DELIMITER );
        this.skipSpaces();
    }

    public double nextFloat() throws IOException
    {
        final StringBuilder token = new StringBuilder();

        // Is there a '+' or '-'?
        this.skipSpaces();
        int ch = this.reader.read();

        if ( ch == '-'
          || ch == '+' )
        {
            token.append( (char) ch );
            ch = this.reader.read();
        }

        // Now read the number itself
        while ( ch != -1
             && ( Character.isDigit( ch )
               || ch == '.' ) )
        {
            token.append( (char) ch );
            ch = this.reader.read();
        }

        this.reader.unread( ch );
        if ( token.length() == 0 ) {
            throw new IOException( "expected float, but next char is: '" + (char) ch + "'" );
        }

        return Double.valueOf( token.toString() );
    }



    public int nextInt() throws IOException
    {
        final StringBuilder token = new StringBuilder();

        // Is there a '+' or '-'?
        this.skipSpaces();
        int ch = this.reader.read();

        if ( ch == '-'
          || ch == '+' )
        {
            token.append( (char) ch );
            ch = this.reader.read();
        }

        // Now read the number itself
        while ( ch != -1
             && Character.isDigit( ch ) )
        {
            token.append( (char) ch );
            ch = this.reader.read();
        }

        this.reader.unread( ch );
        if ( token.length() == 0 ) {
            throw new IOException( "expected int, but next char is: '" + (char) ch + "'" );
        }

        return Integer.valueOf( token.toString() );
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
            throw new IOException( "expected: '" + (char) ch
                                    + "', but found: '" + readChar + "'" );
        }

        return;
    }

    /** @return true if there are quotes ahead, false otherwise. It does not consume anything. */
    private boolean areQuotesAhead() throws IOException
    {
        boolean toret = false;

        this.skipSpaces();

        int ch = this.reader.read();

        if ( ch == '"'
          || ch == '\'' )
        {
            toret = true;
        }

        this.reader.unread( ch );
        return toret;
    }

    /** @return the next name in the JSON stream. */
    public String nextName() throws IOException
    {
        int quotes = -1;
        String toret = null;

        // Check for quotes or double-quotes
        if ( this.areQuotesAhead() ) {
            quotes = this.reader.read();
        }

        toret = this.getToken();

        if ( quotes >= 0 ) {
            if ( this.areQuotesAhead() ) {
                int endQuotes = this.reader.read();

                if ( quotes != endQuotes ) {
                    throw new IOException( "expected: " + ( (char) quotes )
                                            + " got: " +  ( (char) endQuotes ) );
                }
            } else {
                throw new IOException( "expected: " + ( (char) quotes ) );
            }
        }

        this.skipSpaces();
        int colon = this.reader.read();

        if ( colon != Util.NAME_SEPARATOR ) {
            throw new IOException( "expected '"
                                    + Util.NAME_SEPARATOR
                                    + "' after name ('" + toret + "'?)" );
        }

        return toret;
    }

    /** @return the next string, no matter of being enclosed
     *          in single or double quotes, or null if 'null' was found. */
    public String nextString() throws IOException
    {
        String toret = null;
        final StringBuilder buffer = new StringBuilder();
        int ch;
        int quotes = -1;

        this.skipSpaces();

        // Check for null
        final String token = this.getToken();
        if ( !token.equals( Util.NULL_ID ) ) {
            this.reader.unread( token.toCharArray() );

            // Check for quotes or double-quotes
            if ( this.areQuotesAhead() ) {
                quotes = this.reader.read();
            } else {
                throw new IOException( "expected quotes: ' or double quotes: \"" );
            }

            // Read the id inside
            ch = this.reader.read();
            while( ch != -1
                && ch != quotes )
            {
                if ( ch == '\\' ) {
                    this.reader.unread( ch );
                    ch = this.parseSpecialChar();
                }

                buffer.append( (char) ch );
                ch = this.reader.read();
            }

            // Read quotes if they were present
            this.reader.unread( ch );
            if ( quotes != -1 ) {
                this.match( (char) quotes );
            }

            toret = buffer.toString();
        }

        return toret;
    }

    /** @return The read boolean value. */
    public boolean nextBoolean() throws IOException
    {
        boolean toret = false;
        final String token = this.getToken();

        if ( token.equals( Boolean.toString( true ) ) ) {
            toret = true;
        }
        else
        if ( !token.equals( Boolean.toString( false ) ) ) {
            this.reader.unread( token.toCharArray() );
            throw new IOException( "expected boolean, not: '" + token + '\'' );
        }

        return toret;
    }

    /** Reads a null value. */
    public void nextNull() throws IOException
    {
        final String token = this.getToken();

        if ( !token.equals( Util.NULL_ID ) ) {
            this.reader.unread( token.toCharArray() );
            throw new IOException( "expected null, not: '" + token + '\'' );
        }

        return;
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
                    break;
                case '"':
                    break;
                case '\'':
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
