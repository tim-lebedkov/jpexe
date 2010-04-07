
package com.google.code.jpexe;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * TODO
 */
public  class ResourceEntry {
    public int Id;
    public String Name;
    public ResourceDirectory Directory;
    public ResourceDataEntry Data;
    private ResourceDirectory PEResourceDirectory_this;

    public ResourceEntry(int id, ResourceDataEntry data) {
        this.Id = id;
        this.Data = data;
    }

    public ResourceEntry(String name, ResourceDataEntry data) {
        this.Name = name;
        this.Data = data;
    }

    public ResourceEntry(int id, ResourceDirectory dir) {
        this.Id = id;
        this.Directory = dir;
    }

    public ResourceEntry(String name, ResourceDirectory dir) {
        this.Name = name;
        this.Directory = dir;
    }

    public ResourceEntry(ByteBuffer buf) {
        long orgchanpos = buf.position();
        int val = buf.getInt();
        long offsetToData = buf.getInt();
        // 			System.out.println("Entry: Val=" + val);
        // 			System.out.println("       Off=" + offsetToData);

        if (val < 0) {
            val &= 0x7FFFFFFF;
            Name = extractStringAt(buf, val);
            Id = -1;
            //				System.out.println("    String at " + val + " = " + Name);
        } else {
            Id = val;
        }

        if (offsetToData < 0) {
            offsetToData &= 0x7FFFFFFF;
            long orgpos = buf.position();
            /*buf.position((int) (PEResourceDirectory_this.offset +
                    offsetToData)); todo */
            Directory = new ResourceDirectory();
            // TODO setData?
            buf.position((int) orgpos);
        } else {
            Data = new ResourceDataEntry(buf/* todo, offsetToData*/);
        }
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
        if (Name != null) {
            size += (Name.length() * 2) + 2;
        }

        if (Directory != null) {
            size += Directory.diskSize();
        } else if (Data != null) {
            size += Data.diskSize();
        }

        if ((size % 4) > 0) {
            size += 4 - (size % 4);
        }
        return size;
    }

    public void dump(PrintStream out, int level) {
        indent(level, out);
        if (this.Name != null) {
            out.println("Name=" + Name);
        } else {
            out.println("Id=#" + Id);
        }

        indent(level, out);
        if (this.Directory != null) {
            out.println("ENTRY: DIRECTORY POINTER");
            this.Directory.dump(out, level + 1);
        } else {
            out.println("ENTRY: DATA ENTRY");
            Data.dump(out, level + 1);
        }
    }

    private void indent(int level, PrintStream out) {
        for (int i = 0; i < level; i++) {
            out.print("    ");
        }
    }

    public int buildBuffer(ByteBuffer buffer, long virtualBaseOffset,
            int dataOffset) {
        //			System.out.println("Building Resource Entry buffer  " + Name + "/" + Id + " @ " + buffer.position() + " (" + dataOffset + ")");
        if (Name != null) {
            buffer.putInt(dataOffset | 0x80000000);

            int stringoffset = dataOffset;
            ByteBuffer strbuf = ByteBuffer.allocate(Name.length() * 2 + 2);
            strbuf.order(ByteOrder.LITTLE_ENDIAN);

            strbuf.putShort((short) Name.length());
            for (int i = 0; i < Name.length(); i++) {
                strbuf.putShort((short) Name.charAt(i));
            }
            strbuf.position(0);

            long oldpos = buffer.position();
            buffer.position(dataOffset);
            buffer.put(strbuf);
            dataOffset += Name.length() * 2 + 2;
            if ((dataOffset % 4) != 0) {
                dataOffset += 4 - (dataOffset % 4);
            }
            buffer.position((int) oldpos);
        } else {
            buffer.putInt(Id);
        }

        if (Directory != null) {
            buffer.putInt(dataOffset | 0x80000000);

            int oldpos = buffer.position();
            buffer.position(dataOffset);
            // todo int dirsize = Directory.buildBuffer(buffer, virtualBaseOffset);
            // dataOffset = dirsize;
            buffer.position(oldpos);

        } else if (Data != null) {
            buffer.putInt(dataOffset);
            int oldpos = buffer.position();
            buffer.position(dataOffset);
            dataOffset = Data.buildBuffer(buffer, virtualBaseOffset,
                    dataOffset);
            buffer.position(oldpos);
        } else {
            throw new RuntimeException("Directory and Data are both null!");
        }

        return dataOffset;
    }
}
