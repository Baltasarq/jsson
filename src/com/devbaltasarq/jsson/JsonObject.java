// jsson Baltasar MIT License 2018 <baltasarq@gmail.com>
package com.devbaltasarq.jsson;

import java.util.Collection;
import java.util.HashMap;


/**
 * Contains name/value pairs
 * @author baltasarq@gmail.com
 */
public class JsonObject {
    /** Represents a pair of name, value */
    public static class Pair<T> {
        public Pair(String key, T value)
        {
            this.name = key;
            this.value = value;
        }
        
        @Override
        public int hashCode()
        {
            return ( 11 * this.getName().hashCode() )
                    + ( 13 * this.getValue().hashCode() );
        }
        
        @Override
        public boolean equals(Object o)
        {
            boolean toret = false;
            
            if ( o instanceof Pair ) {
                try {
                    Pair<T> op = (Pair<T>) o;
                    
                    if ( this.getName().equals( op.getName() )
                      && this.getValue().equals( op.getValue() ) )
                    {
                        toret = true;
                    }
                } catch(ClassCastException exc) {
                    toret = false;
                }
            }
            
            return toret;
        }
        
        public String getName()
        {
            return this.name;
        }
        
        public T getValue()
        {
            return this.value;
        }
        
        private final String name;
        private final T value;
    }
    
    /** Creates a new JSONObject */
    public JsonObject()
    {
        this.pairs = new HashMap<>();
    }
    
    public void add(Pair p)
    {
        this.pairs.put( p.getName(), p );
    }
    
    public void addAll(Collection<Pair> pcol)
    {
        for (Pair p: pcol) {
            this.add( p );
        }
        
        return;
    }
    
    private HashMap<String, Pair<Object>> pairs;
}
