package com.google.code.jpexe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Record (some continuous data) in a file.
 */
public abstract class BinaryRecord {
    private long location;
    private ByteBuffer data;

    /**
     * @return location of this data in the file (absolute position)
     */
    public long getLocation() {
        return location;
    }

    /**
     * Changes the location of this record in the file.
     *
     * @param location new location in the file
     */
    public void setLocation(long location) {
        this.location = location;
    }

    /**
     * @return the data
     */
    public ByteBuffer getData() {
        return data;
    }

    /**
     * Saves this data to the file.
     *
     * @param ch file
     * @throws IOException if something goes wrong
     */
    public void write(FileChannel ch) throws IOException {
        data.position(0);
        ch.write(data, location);
    }
}
