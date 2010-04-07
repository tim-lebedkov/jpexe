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

/**
 * Directory of icons in the resource section ?
 */
public class ResIconDir implements BinaryRecord {
    private long location;

    private int m_idReserved;   // Reserved (must be 0)
    private int m_idType;       // Resource Type (1 for icons)
    private int m_idCount;      // How many images?
    private IconDirEntry[] m_entries;

    public ByteBuffer getData() {
        ByteBuffer buf = ByteBuffer.allocate(6 + (16 * m_idCount));
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.position(0);

        buf.putShort((short) m_idReserved);
        buf.putShort((short) m_idType);
        buf.putShort((short) m_idCount);

        for (int i = 0; i < m_idCount; i++) {
            ByteBuffer b = m_entries[i].getData();
            b.position(0);
            buf.put(b);
        }

        return buf;
    }

    public IconDirEntry[] getEntries() {
        return m_entries;
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("m_idReserved: " + m_idReserved + "\n");   // Reserved (must be 0)
        out.append("m_idType: " + m_idType + "\n");       // Resource Type (1 for icons)
        out.append("m_idCount: " + m_idCount + "\n");      // How many images?
        out.append("entries: ---- \n");
        for (int i = 0; i < m_entries.length; i++) {
            out.append(m_entries[i].toString());
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
        m_idReserved = buf.getShort();
        m_idType = buf.getShort();
        m_idCount = buf.getShort();

        m_entries = new IconDirEntry[m_idCount];
        for (int i = 0; i < m_idCount; i++) {
            m_entries[i] = new IconDirEntry(buf);
        }
    }
}
