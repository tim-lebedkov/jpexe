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

import java.io.*;
import java.nio.*;

/**
 * A section header in a PE file.
 */
public class SectionHeader implements Cloneable, BinaryRecord {
    /** this field is always 8 bytes long */
    public byte[] ANSI_Name; // Name of the Section. Can be anything (0)(8BYTES)

    // The size of the section when it is mapped to memory. Must be a multiple of 4096. (8)(DWORD)
    public long VirtualSize;

    // An rva to where it should be mapped in memory. (12)(DWORD)
    public long VirtualAddress;

    // The size of the section in the PE file. Must be a multiple of 512 (16)(DWORD)
    public long SizeOfRawData;

    // A file based offset which points to the location of this sections data (20)(DWORD)
    public long PointerToRawData;

    // In EXE's this field is meaningless, and is set 0 (24)(DWORD)
    public long PointerToRelocations;

    // This is the file-based offset of the line number table. This field is only used for debug purposes, and is usualy set to 0 (28)(DWORD)
    public long PointerToLinenumbers;

    // In EXE's this field is meaningless, and is set 0 (32)(WORD)
    public int NumberOfRelocations;

    // The number of line numbers in the line number table for this section. This field is only used for debug purposes, and is usualy set to 0 (34)(WORD)
    public int NumberOfLinenumbers;

    // The kind of data stored in this section ie. Code, Data, Import data, Relocation data (36)(DWORD)
    public long Characteristics;
    
    private long m_baseoffset;

    /**
     * Creates a new instance of SectionHeader
     *
     * @param baseoffset offset of this section header
     */
    public SectionHeader(long baseoffset) {
        m_baseoffset = baseoffset;
    }

    /**
     * Creates a new instance of SectionHeader.
     * 
     * @param name name of the section (no more than 8 ANSI characters)
     */
    public SectionHeader(String name) {
        this.ANSI_Name = new byte[8];
        byte[] bytes = name.getBytes();
        System.arraycopy(bytes, 0, this.ANSI_Name, 0,
                bytes.length);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getName() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            buffer.append((char) ANSI_Name[i]);
        }
        return buffer.toString();
    }

    public void setData(ByteBuffer head) {
        ANSI_Name = new byte[8];
        for (int i = 0; i < 8; i++) {
            ANSI_Name[i] = head.get();
        }

        VirtualSize = head.getInt();
        VirtualAddress = head.getInt();
        SizeOfRawData = head.getInt();
        PointerToRawData = head.getInt();
        PointerToRelocations = head.getInt();
        PointerToLinenumbers = head.getInt();
        NumberOfRelocations = head.getShort();
        NumberOfLinenumbers = head.getShort();
        Characteristics = head.getInt();
    }

    public void dump(PrintStream out) {
        out.println("SECTION:");
        out.print("  Name= ");
        for (int i = 0; i < 8; i++) {
            out.print((char) ANSI_Name[i]);
        }
        out.println("");
        out.println(
                "  VirtualSize= " + VirtualSize
                + "  // 	The size of the section when it is mapped to memory. Must be a multiple of 4096. (8)(DWORD)");
        out.println(
                "  VirtualAddress= " + VirtualAddress
                + "   // 	An rva to where it should be mapped in memory. (12)(DWORD)");
        out.println(
                "  SizeOfRawData= " + SizeOfRawData
                + "   // 	The size of the section in the PE file. Must be a multiple of 512 (16)(DWORD)");
        out.println(
                "  PointerToRawData= " + PointerToRawData
                + "   // 	A file based offset which points to the location of this sections data (20)(DWORD)");
        out.println(
                "  PointerToRelocations= " + PointerToRelocations
                + "   // 	In EXE's this field is meaningless, and is set 0 (24)(DWORD)");
        out.println(
                "  PointerToLinenumbers= " + PointerToLinenumbers
                + "   // 	This is the file-based offset of the line number table. This field is only used for debug purposes, and is usualy set to 0 (28)(DWORD)");
        out.println(
                "  NumberOfRelocations= " + NumberOfRelocations
                + "   // 	In EXE's this field is meaningless, and is set 0 (32)(WORD)");
        out.println(
                "  NumberOfLinenumbers= " + NumberOfLinenumbers
                + "   // 	The number of line numbers in the line number table for this section. This field is only used for debug purposes, and is usualy set to 0 (34)(WORD)");
        out.println(
                "  Characteristics= " + Characteristics
                + "   // 	The kind of data stored in this section ie. Code, Data, Import data, Relocation data (36)(DWORD)");

    }

    public ByteBuffer getData() {
        ByteBuffer head = ByteBuffer.allocate(40);
        head.order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < 8; i++) {
            head.put(ANSI_Name[i]);
        }

        head.putInt((int) VirtualSize);
        head.putInt((int) VirtualAddress);
        head.putInt((int) SizeOfRawData);
        head.putInt((int) PointerToRawData);
        head.putInt((int) PointerToRelocations);
        head.putInt((int) PointerToLinenumbers);
        head.putShort((short) NumberOfRelocations);
        head.putShort((short) NumberOfLinenumbers);
        head.putInt((int) Characteristics);

        head.position(0);
        return head;
    }

    public long getLocation() {
        return m_baseoffset;
    }

    public void setLocation(long location) {
        this.m_baseoffset = location;
    }
}
