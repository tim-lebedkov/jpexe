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
import java.util.*;

/**
 * PE header.
 */
public class Header implements Cloneable, BinaryRecord {
    private long location;

    public int machine; //  4
    public int numberOfSections;     //  6
    public long timeDateStamp; //  8
    public long pointerToSymbolTable;     //  C
    public long numberOfSymbols; // 10
    public int sizeOfOptionalHeader;     // 14
    public int characteristics; // 16

    // Optional Header 
    public int magic;     // 18
    public short majorLinkerVersion;     // 1a
    public short minorLinkerVersion; // 1b
    public long sizeOfCode;     // 1c
    public long sizeOfInitializedData; // 20
    public long sizeOfUninitializedData;     // 24
    public long addressOfEntryPoint; // 28
    public long baseOfCode;     // 2c
    public long baseOfData;    //  NT additional fields.  30
    public long imageBase;     // 34
    public long sectionAlignment; // 38
    public long fileAlignment;     // 3c
    public int majorOperatingSystemVersion; // 40
    public int minorOperatingSystemVersion;     // 42
    public int majorImageVersion; // 44
    public int minorImageVersion;     // 46
    public int majorSubsystemVersion; // 48
    public int minorSubsystemVersion;     // 4a
    public long reserved1;     // 4c
    public long sizeOfImage; // 50
    public long sizeOfHeaders;     // 54
    public long checkSum;     // 58
    public int subsystem; // 5c
    public int dllCharacteristics;     // 5e
    public long sizeOfStackReserve; // 60
    public long sizeOfStackCommit;     // 64
    public long sizeOfHeapReserve; // 68
    public long sizeOfHeapCommit;     // 6c
    public long loaderFlags; // 70
    public long numberOfRvaAndSizes; // 74
    public long exportDirectory_VA; // 78
    public long exportDirectory_Size; // 7c
    public long importDirectory_VA; // 80
    public long importDirectory_Size; // 84
    public long resourceDirectory_VA; // 88
    public long resourceDirectory_Size; // 8c
    public long exceptionDirectory_VA; // 90
    public long exceptionDirectory_Size; // 94
    public long securityDirectory_VA; // 98
    public long securityDirectory_Size; // 9c
    public long baseRelocationTable_VA; // a0
    public long baseRelocationTable_Size; // a4
    public long debugDirectory_VA; // a8
    public long debugDirectory_Size; // ac
    public long architectureSpecificData_VA; // b0
    public long architectureSpecificData_Size; // b4
    public long rvaofGP_VA; // b8
    public long rvaofGP_Size; // bc
    public long tlsDirectory_VA; // c0
    public long tlsDirectory_Size; // c4
    public long loadConfigurationDirectory_VA; // c8
    public long loadConfigurationDirectory_Size; // cc
    public long boundImportDirectoryinheaders_VA; // d0
    public long boundImportDirectoryinheaders_Size; // d4
    public long importAddressTable_VA; // d8
    public long importAddressTable_Size; // dc
    public long delayLoadImportDescriptors_VA; // e0
    public long delayLoadImportDescriptors_Size; // e4
    public long comRuntimedescriptor_VA; // e8
    public long comRuntimedescriptor_Size; // ec

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setData(ByteBuffer head) {
        int pemagic = head.getInt();
        if (pemagic != 17744)
            throw new IllegalArgumentException("Expected 4 byte magic number 17444");

        machine = head.getShort(); //  4
        numberOfSections = head.getShort();     //  6
        timeDateStamp = head.getInt(); //  8
        pointerToSymbolTable = head.getInt();     //  C
        numberOfSymbols = head.getInt(); // 10
        sizeOfOptionalHeader = head.getShort();     // 14
        characteristics = head.getShort(); // 16

        // Optional Header
        magic = head.getShort();     // 18
        majorLinkerVersion = head.get();     // 1a
        minorLinkerVersion = head.get(); // 1b
        sizeOfCode = head.getInt();     // 1c
        sizeOfInitializedData = head.getInt(); // 20
        sizeOfUninitializedData = head.getInt();     // 24
        addressOfEntryPoint = head.getInt(); // 28
        baseOfCode = head.getInt();     // 2c
        baseOfData = head.getInt();    //    // NT additional fields. // 30

        //
        imageBase = head.getInt();     // 34
        sectionAlignment = head.getInt(); // 38
        fileAlignment = head.getInt();     // 3c
        majorOperatingSystemVersion = head.getShort(); // 40
        minorOperatingSystemVersion = head.getShort();     // 42
        majorImageVersion = head.getShort(); // 44
        minorImageVersion = head.getShort();     // 46
        majorSubsystemVersion = head.getShort(); // 48
        minorSubsystemVersion = head.getShort();     // 4a
        reserved1 = head.getInt();     // 4c
        sizeOfImage = head.getInt(); // 50
        sizeOfHeaders = head.getInt();     // 54
        checkSum = head.getInt();     // 58
        subsystem = head.getShort(); // 5c
        dllCharacteristics = head.getShort();     // 5e
        sizeOfStackReserve = head.getInt(); // 60
        sizeOfStackCommit = head.getInt();     // 64
        sizeOfHeapReserve = head.getInt(); // 68
        sizeOfHeapCommit = head.getInt();     // 6c
        loaderFlags = head.getInt(); // 70
        numberOfRvaAndSizes = head.getInt(); // 74

        exportDirectory_VA = head.getInt(); // 78
        exportDirectory_Size = head.getInt(); // 7c
        importDirectory_VA = head.getInt(); // 80
        importDirectory_Size = head.getInt(); // 84
        resourceDirectory_VA = head.getInt(); // 88
        resourceDirectory_Size = head.getInt(); // 8c
        exceptionDirectory_VA = head.getInt(); // 90
        exceptionDirectory_Size = head.getInt(); // 94
        securityDirectory_VA = head.getInt(); // 98
        securityDirectory_Size = head.getInt(); // 9c
        baseRelocationTable_VA = head.getInt(); // a0
        baseRelocationTable_Size = head.getInt(); // a4
        debugDirectory_VA = head.getInt(); // a8
        debugDirectory_Size = head.getInt(); // ac
        architectureSpecificData_VA = head.getInt(); // b0
        architectureSpecificData_Size = head.getInt(); // b4
        rvaofGP_VA = head.getInt(); // b8
        rvaofGP_Size = head.getInt(); // bc
        tlsDirectory_VA = head.getInt(); // c0
        tlsDirectory_Size = head.getInt(); // c4
        loadConfigurationDirectory_VA = head.getInt(); // c8
        loadConfigurationDirectory_Size = head.getInt(); // cc
        boundImportDirectoryinheaders_VA = head.getInt(); // d0
        boundImportDirectoryinheaders_Size = head.getInt(); // d4
        importAddressTable_VA = head.getInt(); // d8
        importAddressTable_Size = head.getInt(); // dc
        delayLoadImportDescriptors_VA = head.getInt(); // e0
        delayLoadImportDescriptors_Size = head.getInt(); // e4
        comRuntimedescriptor_VA = head.getInt(); // e8
        comRuntimedescriptor_Size = head.getInt(); // ec
    }

    /**
     * Outputs debug information
     *
     * @param out output
     */
    public void dump(PrintStream out) {
        out.println("PEHeader:");
        out.println("int  Machine=" + machine + " //  4");
        out.println("int  NumberOfSections=" + numberOfSections + "     //  6");
        out.println("long   TimeDateStamp=" + timeDateStamp + " //  8");
        out.println("long   PointerToSymbolTable=" + pointerToSymbolTable
                + "     //  C");
        out.println("long   NumberOfSymbols=" + numberOfSymbols + " // 10");
        out.println("int  SizeOfOptionalHeader=" + sizeOfOptionalHeader
                + "     // 14");
        out.println("int  Characteristics=" + characteristics + " // 16");
        // Optional Header

        out.println("int    Magic=" + magic + "     // 18");
        out.println("short   MajorLinkerVersion=" + majorLinkerVersion
                + "     // 1a");
        out.println("short   MinorLinkerVersion=" + minorLinkerVersion
                + " // 1b");
        out.println("long   SizeOfCode=" + sizeOfCode + "     // 1c");
        out.println("long   SizeOfInitializedData=" + sizeOfInitializedData
                + " // 20");
        out.println("long   SizeOfUninitializedData=" + sizeOfUninitializedData
                + "     // 24");
        out.println("long   AddressOfEntryPoint=" + addressOfEntryPoint
                + " // 28");
        out.println("long   BaseOfCode=" + baseOfCode + "     // 2c");
        out.println("long   BaseOfData=" + baseOfData
                + "    //    // NT additional fields. // 30");
        //
        out.println("long   ImageBase=" + imageBase + "     // 34");
        out.println("long   SectionAlignment=" + sectionAlignment + " // 38");
        out.println("long   FileAlignment=" + fileAlignment + "     // 3c");
        out.println("int    MajorOperatingSystemVersion="
                + majorOperatingSystemVersion + " // 40");
        out.println("int    MinorOperatingSystemVersion="
                + minorOperatingSystemVersion + "     // 42");
        out.println("int    MajorImageVersion=" + majorImageVersion + " // 44");
        out.println("int    MinorImageVersion=" + minorImageVersion
                + "     // 46");
        out.println("int    MajorSubsystemVersion=" + majorSubsystemVersion
                + " // 48");
        out.println("int    MinorSubsystemVersion=" + minorSubsystemVersion
                + "     // 4a");
        out.println("long   Reserved1=" + reserved1 + "     // 4c");
        out.println("long   SizeOfImage=" + sizeOfImage + " // 50");
        out.println("long   SizeOfHeaders=" + sizeOfHeaders + "     // 54");
        out.println("long   CheckSum=" + checkSum + "     // 58");
        out.println("int    Subsystem=" + subsystem + " // 5c");
        out.println("int    DllCharacteristics=" + dllCharacteristics
                + "     // 5e");
        out.println("long   SizeOfStackReserve=" + sizeOfStackReserve + " // 60");
        out.println("long   SizeOfStackCommit=" + sizeOfStackCommit
                + "     // 64");
        out.println("long   SizeOfHeapReserve=" + sizeOfHeapReserve + " // 68");
        out.println("long   SizeOfHeapCommit=" + sizeOfHeapCommit + "     // 6c");
        out.println("long   LoaderFlags=" + loaderFlags + " // 70");
        out.println("long   NumberOfRvaAndSizes=" + numberOfRvaAndSizes
                + " // 74");

        out.println("long ExportDirectory_VA=" + exportDirectory_VA + " // 78");
        out.println("long ExportDirectory_Size=" + exportDirectory_Size
                + " // 7c");
        out.println("long ImportDirectory_VA=" + importDirectory_VA + " // 80");
        out.println("long ImportDirectory_Size=" + importDirectory_Size
                + " // 84");
        out.println("long ResourceDirectory_VA=" + resourceDirectory_VA
                + " // 88");
        out.println("long ResourceDirectory_Size=" + resourceDirectory_Size
                + " // 8c");
        out.println("long ExceptionDirectory_VA=" + exceptionDirectory_VA
                + " // 90");
        out.println("long ExceptionDirectory_Size=" + exceptionDirectory_Size
                + " // 94");
        out.println("long SecurityDirectory_VA=" + securityDirectory_VA
                + " // 98");
        out.println("long SecurityDirectory_Size=" + securityDirectory_Size
                + " // 9c");
        out.println("long BaseRelocationTable_VA=" + baseRelocationTable_VA
                + " // a0");
        out.println("long BaseRelocationTable_Size=" + baseRelocationTable_Size
                + " // a4");
        out.println("long DebugDirectory_VA=" + debugDirectory_VA + " // a8");
        out.println("long DebugDirectory_Size=" + debugDirectory_Size + " // ac");
        out.println("long ArchitectureSpecificData_VA="
                + architectureSpecificData_VA + " // b0");
        out.println("long ArchitectureSpecificData_Size="
                + architectureSpecificData_Size + " // b4");
        out.println("long RVAofGP_VA=" + rvaofGP_VA + " // b8");
        out.println("long RVAofGP_Size=" + rvaofGP_Size + " // bc");
        out.println("long TLSDirectory_VA=" + tlsDirectory_VA + " // c0");
        out.println("long TLSDirectory_Size=" + tlsDirectory_Size + " // c4");
        out.println("long LoadConfigurationDirectory_VA="
                + loadConfigurationDirectory_VA + " // c8");
        out.println("long LoadConfigurationDirectory_Size="
                + loadConfigurationDirectory_Size + " // cc");
        out.println("long BoundImportDirectoryinheaders_VA="
                + boundImportDirectoryinheaders_VA + " // d0");
        out.println("long BoundImportDirectoryinheaders_Size="
                + boundImportDirectoryinheaders_Size + " // d4");
        out.println("long ImportAddressTable_VA=" + importAddressTable_VA
                + " // d8");
        out.println("long ImportAddressTable_Size=" + importAddressTable_Size
                + " // dc");
        out.println("long DelayLoadImportDescriptors_VA="
                + delayLoadImportDescriptors_VA + " // e0");
        out.println("long DelayLoadImportDescriptors_Size="
                + delayLoadImportDescriptors_Size + " // e4");
        out.println("long COMRuntimedescriptor_VA=" + comRuntimedescriptor_VA
                + " // e8");
        out.println("long COMRuntimedescriptor_Size="
                + comRuntimedescriptor_Size + " // ec");
    }

    public ByteBuffer getData() {
        ByteBuffer head = ByteBuffer.allocate(16 + this.sizeOfOptionalHeader);
        head.order(ByteOrder.LITTLE_ENDIAN);
        head.position(0);

        head.putInt(17744);

        head.putShort((short) machine); //  4
        head.putShort((short) numberOfSections);     //  6
        head.putInt((int) timeDateStamp); //  8
        head.putInt((int) pointerToSymbolTable);     //  C
        head.putInt((int) numberOfSymbols); // 10
        head.putShort((short) sizeOfOptionalHeader);     // 14
        head.putShort((short) characteristics); // 16
        // Optional Header

        head.putShort((short) magic);     // 18
        head.put((byte) majorLinkerVersion);     // 1a
        head.put((byte) minorLinkerVersion); // 1b
        head.putInt((int) sizeOfCode);     // 1c
        head.putInt((int) sizeOfInitializedData); // 20
        head.putInt((int) sizeOfUninitializedData);     // 24
        head.putInt((int) addressOfEntryPoint); // 28
        head.putInt((int) baseOfCode);     // 2c
        head.putInt((int) baseOfData);    //    // NT additional fields. // 30
        //
        head.putInt((int) imageBase);     // 34
        head.putInt((int) sectionAlignment); // 38
        head.putInt((int) fileAlignment);     // 3c
        head.putShort((short) majorOperatingSystemVersion); // 40
        head.putShort((short) minorOperatingSystemVersion);     // 42
        head.putShort((short) majorImageVersion); // 44
        head.putShort((short) minorImageVersion);     // 46
        head.putShort((short) majorSubsystemVersion); // 48
        head.putShort((short) minorSubsystemVersion);     // 4a
        head.putInt((int) reserved1);     // 4c
        head.putInt((int) sizeOfImage); // 50
        head.putInt((int) sizeOfHeaders);     // 54
        head.putInt((int) checkSum);     // 58
        head.putShort((short) subsystem); // 5c
        head.putShort((short) dllCharacteristics);     // 5e
        head.putInt((int) sizeOfStackReserve); // 60
        head.putInt((int) sizeOfStackCommit);     // 64
        head.putInt((int) sizeOfHeapReserve); // 68
        head.putInt((int) sizeOfHeapCommit);     // 6c
        head.putInt((int) loaderFlags); // 70
        head.putInt((int) numberOfRvaAndSizes); // 74

        head.putInt((int) exportDirectory_VA); // 78
        head.putInt((int) exportDirectory_Size); // 7c
        head.putInt((int) importDirectory_VA); // 80
        head.putInt((int) importDirectory_Size); // 84
        head.putInt((int) resourceDirectory_VA); // 88
        head.putInt((int) resourceDirectory_Size); // 8c
        head.putInt((int) exceptionDirectory_VA); // 90
        head.putInt((int) exceptionDirectory_Size); // 94
        head.putInt((int) securityDirectory_VA); // 98
        head.putInt((int) securityDirectory_Size); // 9c
        head.putInt((int) baseRelocationTable_VA); // a0
        head.putInt((int) baseRelocationTable_Size); // a4
        head.putInt((int) debugDirectory_VA); // a8
        head.putInt((int) debugDirectory_Size); // ac
        head.putInt((int) architectureSpecificData_VA); // b0
        head.putInt((int) architectureSpecificData_Size); // b4
        head.putInt((int) rvaofGP_VA); // b8
        head.putInt((int) rvaofGP_Size); // bc
        head.putInt((int) tlsDirectory_VA); // c0
        head.putInt((int) tlsDirectory_Size); // c4
        head.putInt((int) loadConfigurationDirectory_VA); // c8
        head.putInt((int) loadConfigurationDirectory_Size); // cc
        head.putInt((int) boundImportDirectoryinheaders_VA); // d0
        head.putInt((int) boundImportDirectoryinheaders_Size); // d4
        head.putInt((int) importAddressTable_VA); // d8
        head.putInt((int) importAddressTable_Size); // dc
        head.putInt((int) delayLoadImportDescriptors_VA); // e0
        head.putInt((int) delayLoadImportDescriptors_Size); // e4
        head.putInt((int) comRuntimedescriptor_VA); // e8
        head.putInt((int) comRuntimedescriptor_Size); // ec

        head.position(0);
        return head;
    }

    public void updateVAAndSize(List<SectionHeader> oldsections,
            List<SectionHeader> newsections) {
        long codebase = findNewVA(this.baseOfCode, oldsections, newsections);
        long codesize = findNewSize(this.baseOfCode, oldsections, newsections);
        //	System.out.println("New baseOfCode=" + codebase + " (size=" + codesize + ")");
        this.baseOfCode = codebase;
        this.sizeOfCode = codesize;

        this.addressOfEntryPoint = findNewVA(this.addressOfEntryPoint,
                oldsections, newsections);

        long database = findNewVA(this.baseOfData, oldsections, newsections);
        long datasize = findNewSize(this.baseOfData, oldsections, newsections);
        //	System.out.println("New baseOfData=" + database + " (size=" + datasize + ")");
        this.baseOfData = database;

        long imagesize = 0;
        for (int i = 0; i < newsections.size(); i++) {
            SectionHeader sect = newsections.get(i);
            long curmax = sect.virtualAddress + sect.virtualSize;
            if (curmax > imagesize) {
                imagesize = curmax;
            }
        }
        this.sizeOfImage = imagesize;

        //	this.sizeOfInitializedData = datasize;

        exportDirectory_Size = findNewSize(exportDirectory_VA, oldsections,
                newsections);
        exportDirectory_VA = findNewVA(exportDirectory_VA, oldsections,
                newsections);
        importDirectory_Size = findNewSize(importDirectory_VA, oldsections,
                newsections);
        importDirectory_VA = findNewVA(importDirectory_VA, oldsections,
                newsections);
        resourceDirectory_Size = findNewSize(resourceDirectory_VA, oldsections,
                newsections);
        resourceDirectory_VA = findNewVA(resourceDirectory_VA, oldsections,
                newsections);
        exceptionDirectory_Size = findNewSize(exceptionDirectory_VA, oldsections,
                newsections);
        exceptionDirectory_VA = findNewVA(exceptionDirectory_VA, oldsections,
                newsections);
        securityDirectory_Size = findNewSize(securityDirectory_VA, oldsections,
                newsections);
        securityDirectory_VA = findNewVA(securityDirectory_VA, oldsections,
                newsections);
        baseRelocationTable_Size = findNewSize(baseRelocationTable_VA,
                oldsections, newsections);
        baseRelocationTable_VA = findNewVA(baseRelocationTable_VA, oldsections,
                newsections);
        debugDirectory_Size = findNewSize(debugDirectory_VA, oldsections,
                newsections);
        debugDirectory_VA = findNewVA(debugDirectory_VA, oldsections,
                newsections);
        architectureSpecificData_Size = findNewSize(architectureSpecificData_VA,
                oldsections, newsections);
        architectureSpecificData_VA = findNewVA(architectureSpecificData_VA,
                oldsections, newsections);
        rvaofGP_Size = findNewSize(rvaofGP_VA, oldsections, newsections);
        rvaofGP_VA = findNewVA(rvaofGP_VA, oldsections, newsections);
        tlsDirectory_Size = findNewSize(tlsDirectory_VA, oldsections,
                newsections);
        tlsDirectory_VA = findNewVA(tlsDirectory_VA, oldsections, newsections);
        loadConfigurationDirectory_Size = findNewSize(
                loadConfigurationDirectory_VA, oldsections, newsections);
        loadConfigurationDirectory_VA = findNewVA(loadConfigurationDirectory_VA,
                oldsections, newsections);
        boundImportDirectoryinheaders_Size = findNewSize(
                boundImportDirectoryinheaders_VA, oldsections, newsections);
        boundImportDirectoryinheaders_VA = findNewVA(
                boundImportDirectoryinheaders_VA, oldsections, newsections);
        importAddressTable_Size = findNewSize(importAddressTable_VA, oldsections,
                newsections);
        importAddressTable_VA = findNewVA(importAddressTable_VA, oldsections,
                newsections);
        delayLoadImportDescriptors_Size = findNewSize(
                delayLoadImportDescriptors_VA, oldsections, newsections);
        delayLoadImportDescriptors_VA = findNewVA(delayLoadImportDescriptors_VA,
                oldsections, newsections);
        comRuntimedescriptor_Size = findNewSize(comRuntimedescriptor_VA,
                oldsections, newsections);
        comRuntimedescriptor_VA = findNewVA(comRuntimedescriptor_VA, oldsections,
                newsections);
    }

    private long findNewVA(long current, List<SectionHeader> oldsections,
            List<SectionHeader> newsections) {
        for (int i = 0; i < oldsections.size(); i++) {
            SectionHeader sect = oldsections.get(i);
            if (sect.virtualAddress == current) {
                SectionHeader newsect = newsections.get(i);

                //			System.out.println("Translation VA found for " + current + " = " + i + " (" +newsect.virtualAddress + ")=" + newsect.getName());
                return newsect.virtualAddress;
            } else if ((current > sect.virtualAddress) && (current < (sect.virtualAddress
                    + sect.virtualSize))) {
                long diff = current - sect.virtualAddress;
                SectionHeader newsect = newsections.get(i);
                //			System.out.println("Translation VA found INSIDE " + current + " = " + i + " (" +newsect.virtualAddress + ")=" + newsect.getName());
                return newsect.virtualAddress + diff;
            }
        }
        return 0;
    }

    private long findNewSize(long current, List<SectionHeader> oldsections,
            List<SectionHeader> newsections) {
        for (int i = 0; i < oldsections.size(); i++) {
            SectionHeader sect = oldsections.get(i);
            if (sect.virtualAddress == current) {
                SectionHeader newsect = newsections.get(i);
                //			System.out.println("Translation Size found for " + current + " = " + i + " (" +newsect.virtualAddress + ")=" + newsect.getName());
                //			System.out.println("         Old size " + sect.virtualSize + " vs new size " + newsect.virtualSize);
                return newsect.virtualSize;
            }
        }
        return 0;
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        if (location % 8 != 0)
            throw new IllegalArgumentException(
                    "PE header location must be aligned on 8 byte boundary");
        this.location = location;
    }
}
