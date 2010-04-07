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

import java.nio.*;
import java.util.Map;

/**
 * Directory of icons in the resource section ?
 */
public class ResIconDir implements BinaryRecord {
    private long location;

    private int idReserved;   // Reserved (must be 0)
    private int idType;       // Resource Type (1 for icons)
    private int idCount;      // How many images?
    private IconDirEntry[] entries;

    public ByteBuffer getData() {
        ByteBuffer buf = ByteBuffer.allocate(6 + (16 * idCount));
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.position(0);

        buf.putShort((short) idReserved);
        buf.putShort((short) idType);
        buf.putShort((short) idCount);

        for (int i = 0; i < idCount; i++) {
            ByteBuffer b = entries[i].getData();
            b.position(0);
            buf.put(b);
        }

        return buf;
    }

    public IconDirEntry[] getEntries() {
        return entries;
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("m_idReserved: " + idReserved + "\n");   // Reserved (must be 0)
        out.append("m_idType: " + idType + "\n");       // Resource Type (1 for icons)
        out.append("m_idCount: " + idCount + "\n");      // How many images?
        out.append("entries: ---- \n");
        for (int i = 0; i < entries.length; i++) {
            out.append(entries[i].toString());
        }

        return out.toString();
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public void setData(ByteBuffer buf) {
        idReserved = buf.getShort();
        idType = buf.getShort();
        idCount = buf.getShort();

        entries = new IconDirEntry[idCount];
        for (int i = 0; i < idCount; i++) {
            entries[i] = new IconDirEntry(buf);
        }
    }

    public void materialize(Map<String, Object> lookup) {
    }
}
