/*
 * jpexe
 * Copyright (C) 2003-2010 see http://code.google.com/p/jpexe/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package com.google.code.jpexe;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * Navigable directory in the resource section in a PE.
 */
public class ResourceDirectory implements BinaryRecord {
    public long offset;

    public long virtualBaseOffset;
    public final long virtualAddress;
    public final long pointerToRawData;

    ImageResourceDirectory m_root;

    /**
     * Creates a new instance of ResourceDirectory
     *
     * @param offset offset in the file
     */
    public ResourceDirectory(long offset, long virtualBaseOffset,
            long pointerToRawData, long virtualAddress) {
        this.offset = offset;
        this.virtualBaseOffset = virtualBaseOffset;
        this.pointerToRawData = pointerToRawData;
        this.virtualAddress = virtualAddress;
    }

    public long getLocation() {
        return offset;
    }

    public void setLocation(long location) {
        this.offset = location;
    }

    public ByteBuffer getData() {
        int resourceSize = m_root.diskSize();
        ByteBuffer resbuf = ByteBuffer.allocate(resourceSize);
        resbuf.order(ByteOrder.LITTLE_ENDIAN);
        resbuf.position(0);
        m_root.buildBuffer(resbuf, virtualBaseOffset);
        return resbuf;
    }

    public void setData(ByteBuffer data) {
        m_root = new ImageResourceDirectory(
                data);
    }

    public void dump(PrintStream out) {
        m_root.dump(out, 0);
    }

    public int size() {
        return m_root.diskSize();
    }

    public ImageResourceDirectory getRoot() {
        return m_root;
    }
}
