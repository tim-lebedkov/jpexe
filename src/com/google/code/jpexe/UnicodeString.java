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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

/**
 * A Unicode string.
 */
public class UnicodeString implements BinaryRecord {
    private long location;
    private String data = "";

    /**
     * Empty string.
     */
    public UnicodeString() {
        this.data = "";
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public ByteBuffer getData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setData(ByteBuffer data) {
        short size = data.getShort();
        ByteBuffer buffer = ByteBuffer.allocate(size * 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        data.get(buffer.array());

        StringBuilder buf = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int c = buffer.getShort();
            buf.append((char) c);
        }

        this.data = buf.toString();
    }

    /**
     * @return content
     */
    public String getText() {
        return this.data;
    }

    public void materialize(Map<String, Object> lookup) {
    }
}
