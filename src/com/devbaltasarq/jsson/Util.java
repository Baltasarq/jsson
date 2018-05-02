package com.devbaltasarq.jsson;

class Util {
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
