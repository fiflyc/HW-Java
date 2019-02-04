package com.example.Trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Serializable is an interface containing methods what can code a class into bites and decode from bytes */
interface Serializable {

    /**
     * Code an object into bytes
     * @param out -- OutputStream object what takes coding result
     */
    void serialize(OutputStream out) throws IOException;

    /**
     * Decode an object from bytes
     * @param in -- InputStream object what gets a code
     */
    void deserialize(InputStream in) throws IOException;
}