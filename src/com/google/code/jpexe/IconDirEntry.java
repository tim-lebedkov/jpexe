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

/**
 * One entry in a directory of resource icons.
 */
public class IconDirEntry {
    public short width; // Width, in pixels, of the image
    public short height; // Height, in pixels, of the image
    public short colorCount; // Number of colors in image (0 if >=8bpp)
    public short reserved; // Reserved ( must be 0)
    public int planes; // Color Planes
    public int bitCount; // Bits per pixel
    public long bytesInRes; // How many bytes in this resource?
    public int imageOffset; // Where in the file is this image?

    public IconDirEntry(ByteBuffer buf) {
        width = buf.get();
        height = buf.get();
        colorCount = buf.get();
        reserved = buf.get();
        planes = buf.getShort();
        bitCount = buf.getShort();
        bytesInRes = buf.getInt();
        imageOffset = buf.getShort();
    }

    public ByteBuffer getData() {
        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.position(0);
        buf.put((byte) width);
        buf.put((byte) height);
        buf.put((byte) colorCount);
        buf.put((byte) reserved);
        buf.putShort((short) planes);
        buf.putShort((short) bitCount);
        buf.putInt((int) bytesInRes);
        buf.putShort((short) imageOffset);
        buf.position(0);
        return buf;
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("bWidth: " + width + "\n"); // Width, in pixels, of the image
        out.append("bHeight: " + height + "\n"); // Height, in pixels, of the image
        out.append("bColorCount: " + colorCount + "\n"); // Number of colors in image (0 if >=8bpp)
        out.append("bReserved: " + reserved + "\n"); // Reserved ( must be 0)
        out.append("wPlanes: " + planes + "\n"); // Color Planes
        out.append("wBitCount: " + bitCount + "\n"); // Bits per pixel
        out.append("dwBytesInRes: " + bytesInRes + "\n"); // How many bytes in this resource?
        out.append("dwImageOffset: " + imageOffset + "\n"); // Where in the file is this image?
        return out.toString();
    }
}
