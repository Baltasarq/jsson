package com.devbaltasarq.jsson;

import java.util.Stack;

class Util {
    static class State {
        enum Status {
            TOP_LEVEL,
            WAITING_FOR_BEGINNING,
            IN_OBJECT,
            IN_ARRAY,
            WAITING_FOR_NAME,
            WAITING_FOR_VALUE,
            END_OBJECT,
            END_ARRAY,
        }

        State()
        {
            this.status.push( Status.TOP_LEVEL );
        }

        /** Changes to a new status.
          * @param newStatus A new status to change the parsing to.
          */
        public void changeTo(Status newStatus) throws JsonException
        {
            if ( newStatus == Status.IN_OBJECT ) {
                if ( status.peek() == Status.WAITING_FOR_NAME
                  || status.peek() == Status.WAITING_FOR_BEGINNING
                  || status.peek() == Status.TOP_LEVEL )
                {
                    this.status.push( newStatus );
                } else {
                    throw new JsonException( "expecting {, [ or name before " + newStatus );
                }
            }
            else
            if ( newStatus == Status.IN_ARRAY ) {
                if ( status.peek() == Status.WAITING_FOR_NAME
                  || status.peek() == Status.WAITING_FOR_BEGINNING
                  || status.peek() == Status.TOP_LEVEL )
                {
                    this.status.push( newStatus );
                } else {
                    throw new JsonException( "expecting {, [ or name before " + newStatus );
                }
            }
            else
            if ( newStatus == Status.END_OBJECT ) {
                if ( status.peek() == Status.WAITING_FOR_NAME
                  || status.peek() == Status.IN_ARRAY )
                {
                    this.status.pop();
                } else {
                    throw new JsonException( "expecting {, [ or name before " + newStatus );
                }
            }
            else
            if ( newStatus == Status.END_ARRAY ) {
                if ( status.peek() == Status.WAITING_FOR_VALUE
                  || status.peek() == Status.IN_ARRAY )
                {
                    this.status.pop();
                } else {
                    throw new JsonException( "expecting [ or value before " + newStatus );
                }
            }
        }

        private Stack<Status> status;
    }

    final static String NULL_ID = "null";
    final static char OPEN_OBJECT_DELIMITER = '{';
    final static char END_OBJECT_DELIMITER = '}';
    final static char OPEN_ARRAY_DELIMITER = '[';
    final static char END_ARRAY_DELIMITER = ']';
    final static char NAME_SEPARATOR = ':';
    final static char ENTITY_SEPARATOR = ',';
    final static char QUOTES = '"';
    final static String EXTRA_SEPARATORS =  ENTITY_SEPARATOR + "\012";      // 10 for 1013 sequences.
}
