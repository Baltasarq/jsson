// jsson Baltasar MIT License 2018 <baltasarq@gmail.com>
package com.devbaltasarq.jsson;

import java.io.BufferedReader;
import java.io.Reader;

/**
 * A Java JSON library reader
 * @author baltasarq
 */
public class JsonParser {
    public JsonParser(Reader rd)
    {
        this.reader = new BufferedReader( rd );
    }
    
    private BufferedReader reader;
}
