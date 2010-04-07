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
    public short bWidth; // Width, in pixels, of the image
    public short bHeight; // Height, in pixels, of the image
    public short bColorCount; // Number of colors in image (0 if >=8bpp)
    public short bReserved; // Reserved ( must be 0)
    public int wPlanes; // Color Planes
    public int wBitCount; // Bits per pixel
    public long dwBytesInRes; // How many bytes in this resource?
    public int dwImageOffset; // Where in the file is this image?

    public IconDirEntry(ByteBuffer buf) {
        bWidth = buf.get();
        bHeight = buf.get();
        bColorCount = buf.get();
        bReserved = buf.get();
        wPlanes = buf.getShort();
        wBitCount = buf.getShort();
        dwBytesInRes = buf.getInt();
        dwImageOffset = buf.getShort();
    }

    public ByteBuffer getData() {
        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.position(0);
        buf.put((byte) bWidth);
        buf.put((byte) bHeight);
        buf.put((byte) bColorCount);
        buf.put((byte) bReserved);
        buf.putShort((short) wPlanes);
        buf.putShort((short) wBitCount);
        buf.putInt((int) dwBytesInRes);
        buf.putShort((short) dwImageOffset);
        buf.position(0);
        return buf;
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("bWidth: " + bWidth + "\n"); // Width, in pixels, of the image
        out.append("bHeight: " + bHeight + "\n"); // Height, in pixels, of the image
        out.append("bColorCount: " + bColorCount + "\n"); // Number of colors in image (0 if >=8bpp)
        out.append("bReserved: " + bReserved + "\n"); // Reserved ( must be 0)
        out.append("wPlanes: " + wPlanes + "\n"); // Color Planes
        out.append("wBitCount: " + wBitCount + "\n"); // Bits per pixel
        out.append("dwBytesInRes: " + dwBytesInRes + "\n"); // How many bytes in this resource?
        out.append("dwImageOffset: " + dwImageOffset + "\n"); // Where in the file is this image?
        return out.toString();
    }
}
