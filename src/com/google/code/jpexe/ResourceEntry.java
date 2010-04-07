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
import java.nio.ByteOrder;

/**
 * Entry in a resource directory.
 */
public class ResourceEntry implements BinaryRecord {
    private long location;

    public int id;
    public String name;
    public ResourceDirectory directory;
    public ResourceDataEntry data;
    private ResourceDirectory resourceDirectory_this;

    public ResourceEntry(int id, ResourceDataEntry data) {
        this.id = id;
        this.data = data;
    }

    public ResourceEntry(String name, ResourceDataEntry data) {
        this.name = name;
        this.data = data;
    }

    public ResourceEntry(int id, ResourceDirectory dir) {
        this.id = id;
        this.directory = dir;
    }

    public ResourceEntry(String name, ResourceDirectory dir) {
        this.name = name;
        this.directory = dir;
    }

    public String extractStringAt(ByteBuffer chan, int offset) {
        long orgchanpos = chan.position();
        // TODO chan.position((int) (PEResourceDirectory_this.offset + offset));

        /* todo
        ByteBuffer sizebuf = ByteBuffer.allocate(2);
        sizebuf.order(ByteOrder.LITTLE_ENDIAN);
        chan.read(sizebuf);
        sizebuf.position(0);

        int size = sizebuf.getShort();
        ByteBuffer buffer = ByteBuffer.allocate(size * 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        chan.read(buffer);
        buffer.position(0);

        StringBuffer buf = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            int c = buffer.getShort();
            buf.append((char) c);
        }

        chan.position(orgchanpos);
        return buf.toString();*/
        return "a";
    }

    public int diskSize() {
        int size = 8;
        if (name != null) {
            size += (name.length() * 2) + 2;
        }

        if (directory != null) {
            size += directory.diskSize();
        } else if (data != null) {
            size += data.diskSize();
        }

        if ((size % 4) > 0) {
            size += 4 - (size % 4);
        }
        return size;
    }

    public void dump(PrintStream out, int level) {
        indent(level, out);
        if (this.name != null) {
            out.println("Name=" + name);
        } else {
            out.println("Id=#" + id);
        }

        indent(level, out);
        if (this.directory != null) {
            out.println("ENTRY: DIRECTORY POINTER");
            this.directory.dump(out, level + 1);
        } else {
            out.println("ENTRY: DATA ENTRY");
            data.dump(out, level + 1);
        }
    }

    private void indent(int level, PrintStream out) {
        for (int i = 0; i < level; i++) {
            out.print("    ");
        }
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public ByteBuffer getData() {
        long virtualBaseOffset = 0; // TODO
        int dataOffset = 0; // TODO
        ByteBuffer buffer = ByteBuffer.allocate(100); // TODO
        //			System.out.println("Building Resource Entry buffer  " + name + "/" + id + " @ " + buffer.position() + " (" + dataOffset + ")");
        if (name != null) {
            buffer.putInt(dataOffset | 0x80000000);

            int stringoffset = dataOffset;
            ByteBuffer strbuf = ByteBuffer.allocate(name.length() * 2 + 2);
            strbuf.order(ByteOrder.LITTLE_ENDIAN);

            strbuf.putShort((short) name.length());
            for (int i = 0; i < name.length(); i++) {
                strbuf.putShort((short) name.charAt(i));
            }
            strbuf.position(0);

            long oldpos = buffer.position();
            buffer.position(dataOffset);
            buffer.put(strbuf);
            dataOffset += name.length() * 2 + 2;
            if ((dataOffset % 4) != 0) {
                dataOffset += 4 - (dataOffset % 4);
            }
            buffer.position((int) oldpos);
        } else {
            buffer.putInt(id);
        }

        if (directory != null) {
            buffer.putInt(dataOffset | 0x80000000);

            int oldpos = buffer.position();
            buffer.position(dataOffset);
            // todo int dirsize = directory.buildBuffer(buffer, virtualBaseOffset);
            // dataOffset = dirsize;
            buffer.position(oldpos);

        } else if (data != null) {
            buffer.putInt(dataOffset);
            int oldpos = buffer.position();
            buffer.position(dataOffset);
            /* todo dataOffset = data.buildBuffer(buffer, virtualBaseOffset,
                    dataOffset);*/
            buffer.position(oldpos);
        } else {
            throw new RuntimeException("Directory and Data are both null!");
        }

        return buffer;
    }

    public void setData(ByteBuffer buf) {
        long orgchanpos = buf.position();
        int val = buf.getInt();
        long offsetToData = buf.getInt();
        // 			System.out.println("Entry: Val=" + val);
        // 			System.out.println("       Off=" + offsetToData);

        if (val < 0) {
            val &= 0x7FFFFFFF;
            name = extractStringAt(buf, val);
            id = -1;
            //				System.out.println("    String at " + val + " = " + name);
        } else {
            id = val;
        }

        if (offsetToData < 0) {
            offsetToData &= 0x7FFFFFFF;
            long orgpos = buf.position();
            /*buf.position((int) (PEResourceDirectory_this.offset +
                    offsetToData)); todo */
            directory = new ResourceDirectory();
            // TODO setData?
            buf.position((int) orgpos);
        } else {
            // TODO data = new ResourceDataEntry(buf/* todo, offsetToData*/);
        }
    }
}
