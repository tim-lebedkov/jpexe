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

import java.io.PrintStream;
import java.nio.ByteBuffer;

/**
 * Data for a resource entry.
 */
public class ResourceDataEntry implements BinaryRecord {
    private long location;

    long OffsetToData; // To update at each change
    long Size;
    long CodePage; // never changed
    long Reserved; // never changed
    ByteBuffer Data;

    public int diskSize() {
        int size = 16 + (int) this.Size;
        if ((size % 4) > 0) {
            size += 4 - (size % 4);
        }
        return size;
    }

    public void dump(PrintStream out, int level) {
        indent(level, out);
        out.println("OffsetToData=" + OffsetToData);
        indent(level, out);
        out.println("Size=" + Size);
        indent(level, out);
        out.println("CodePage=" + CodePage);
        indent(level, out);
        out.println("Reserved=" + Reserved);
        indent(level, out);
        out.print("Data={ ");
        for (int i = 0; i < this.Data.capacity();
                i++) {
            out.print("" + Integer.toHexString(Data.get(i)) + ",");
        }
        out.println(" }");
    }

    private void indent(int level, PrintStream out) {
        for (int i = 0; i < level;
                i++) {
            out.print("    ");
        }
    }

    public void setData(ByteBuffer data) {
        Data = data;
        Size = data.capacity();
        ByteBuffer buf = data;
        OffsetToData = buf.getInt();
        Size = buf.getInt();
        CodePage = buf.getInt();
        Reserved = buf.getInt();
        /* todo
        long datapos = PEResourceDirectory.this.pointerToRawData + (OffsetToData
        - PEResourceDirectory.this.virtualAddress);
        Data = ByteBuffer.allocate((int) Size);
        Data.order(ByteOrder.LITTLE_ENDIAN);
        chan.position(datapos);
        chan.read(Data);
        Data.position(0);
        chan.position(orgpos);
         *
         */
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(100); // TODO
        long virtualBaseOffset = 0; // TODO
        int dataOffset = 0; // TODO
        //			System.out.println("Building Data Entry buffer @ " + buffer.position() + " (" + dataOffset + ")");
        dataOffset = buffer.position() + 16;
        buffer.putInt((int) (dataOffset + virtualBaseOffset));
        buffer.putInt((int) Size);
        buffer.putInt((int) CodePage);
        buffer.putInt((int) Reserved);
        Data.position(0);
        buffer.put(Data);
        dataOffset += Size;
        if ((dataOffset % 4) > 0) {
            dataOffset += (4 - (dataOffset % 4));
        }
        return buffer;
    }
}
