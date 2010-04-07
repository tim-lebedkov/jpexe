package com.google.code.jpexe;

import java.io.PrintStream;
import java.nio.ByteBuffer;

public class ResourceDataEntry {
    long OffsetToData; // To update at each change
    long Size;
    long CodePage; // never changed
    long Reserved; // never changed
    ByteBuffer Data;

    public ResourceDataEntry(ByteBuffer buf) {
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

    public int buildBuffer(ByteBuffer buffer, long virtualBaseOffset,
            int dataOffset) {
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
        return dataOffset;
    }

    public void setData(ByteBuffer data) {
        Data = data;
        Size = data.capacity();
    }
}
