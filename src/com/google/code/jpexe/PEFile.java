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

import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.FileChannel.MapMode;

/**
 * Portable executable.
 * 
 * http://de.wikipedia.org/wiki/Portable_Executable
 */
public class PEFile {
    private File file;
    private FileInputStream in = null;
    private FileChannel channel = null;
    private MappedByteBuffer mbb;

    private OldMSHeader oldMSDOSHeader;

    /**
     * Data between the old MS-DOS header (64 bytes long) and the new
     * PE header (starting with 'PE'\0\0). Contains also the MS-DOS 2.0 stub
     * program.
     */
    private SimpleBinaryRecord header2;

    public Header header;

    private List<SectionHeader> sections = new ArrayList<SectionHeader>();

    private ResourceDirectory resourceDir;

    /**
     * Creates a new instance of PEFile
     *
     * @param f an .exe
     */
    public PEFile(File f) {
        file = f;
    }

    /**
     * @return sections of the file. The returned list should not be modified
     *     directly.
     */
    public List<SectionHeader> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public void close() throws IOException {
        in.close();
        in = null;
        mbb = null;
    }

    public void open() throws IOException {
        in = new FileInputStream(file);
        channel = in.getChannel();

        mbb = channel.map(MapMode.READ_ONLY, 0, channel.size());
        mbb.order(ByteOrder.LITTLE_ENDIAN);

        // read the MS-DOS header (starts with 'MZ')
        oldMSDOSHeader = new OldMSHeader();
        oldMSDOSHeader.setData(mbb);

        // read everything between 2 headers (including the MS-DOS 2.0 stub)
        this.header2 = new SimpleBinaryRecord(oldMSDOSHeader.e_lfanew -
                mbb.position());
        this.header2.setData(mbb);

        // read PE header (starts with 'PE')
        header = new Header();
        header.setData(mbb);

        int seccount = header.NumberOfSections;
        int headoffset = oldMSDOSHeader.e_lfanew;
        long offset = headoffset + (header.NumberOfRvaAndSizes * 8) + 24 + 96;

        for (int i = 0; i < seccount; i++) {
            SectionHeader sect = new SectionHeader(offset);
            mbb.position((int) offset);
            sect.setData(mbb);
            sections.add(sect);
            offset += 40;
        }

        ByteBuffer resbuf = null;
        long resourceoffset = header.ResourceDirectory_VA;
        for (int i = 0; i < seccount; i++) {
            SectionHeader sect = sections.get(i);
            if (sect.VirtualAddress == resourceoffset) {
                // TODO
                ResourceDirectory prd = new ResourceDirectory(
                        sect.PointerToRawData, sect.VirtualAddress,
                        sect.PointerToRawData, sect.VirtualAddress);
                // TODO resbuf = prd.buildResource(sect.VirtualAddress);
                break;
            }
        }
    }

    public FileChannel getChannel() {
        return channel;
    }

    /**
     * Returns the existing resource directory.
     *
     * @return resource directory or null if it does not exist
     */
    public ResourceDirectory getResourceDirectory() {
        if (resourceDir != null) {
            return resourceDir;
        }

        long resourceoffset = header.ResourceDirectory_VA;
        for (int i = 0; i < sections.size(); i++) {
            SectionHeader sect = sections.get(i);
            if (sect.VirtualAddress == resourceoffset) {
                resourceDir = new ResourceDirectory(
                        sect.PointerToRawData, sect.VirtualAddress,
                        sect.PointerToRawData,
                        sect.VirtualAddress);
                mbb.position((int) sect.PointerToRawData);
                resourceDir.setData(mbb);
                return resourceDir;
            }
        }

        return null;
    }

    /**
     * Adds a new section
     *
     * @param s new section. VirtualAddress will be set automatically.
     */
    public void addSectionHeader(SectionHeader s) {
        long va = -1;
        for (SectionHeader s2: this.sections) {
            if (s2.VirtualAddress > va) {
                va = s2.VirtualAddress;
                s.VirtualAddress = s2.VirtualAddress + s2.VirtualSize;
            }
        }

        this.sections.add(s);
        this.header.NumberOfSections = this.sections.size();
    }

    public void dumpTo(File destination) throws IOException,
            CloneNotSupportedException {
        int outputcount = 0;
        FileOutputStream fos = new FileOutputStream(destination);
        FileChannel out = fos.getChannel();

        // Make a copy of the Header, for safe modifications
        List<SectionHeader> shs = new ArrayList<SectionHeader>();
        for (int i = 0; i < shs.size(); i++) {
            SectionHeader sect = shs.get(i);
            SectionHeader cs = (SectionHeader) sect.clone();
            shs.add(cs);
        }

        // First, write the old MS Header, the one starting
        // with "MZ"...
        ByteBuffer bb = this.oldMSDOSHeader.getData();
        bb.position(0);
        outputcount = out.write(bb);

        // write everything between 2 headers
        bb = this.header2.getData();
        bb.position(0);
        out.write(bb);

        // Then Write the new Header...
        bb = this.header.getData();
        bb.position(0);
        out.write(bb);

        // After the header, there are all the section
        // headers...
        long offset = this.oldMSDOSHeader.e_lfanew +
                (header.NumberOfRvaAndSizes * 8)
                + 24 + 96;
        out.position(offset);
        for (int i = 0; i < shs.size(); i++) {
            SectionHeader sh = shs.get(i);
            ByteBuffer buf = sh.getData();
            outputcount = out.write(buf);
        }

        // Now, we write the real data: each of the section
        // and their data...

        // Not sure why it's always at 1024... ?
        offset = 1024;

        long virtualAddress = offset;
        if ((virtualAddress % this.header.SectionAlignment) > 0) {
                virtualAddress += this.header.SectionAlignment -
                (virtualAddress % this.header.SectionAlignment);
        }

        // Dump each section data
        long resourceoffset = header.ResourceDirectory_VA;
        for (int i = 0; i < shs.size(); i++) {
            SectionHeader sect = shs.get(i);
            if (resourceoffset == sect.VirtualAddress) {
                // System.out.println("Dumping RES section " + i + " at " + offset + " from " + sect.PointerToRawData + " (VA=" + virtualAddress + ")");
                out.position(offset);
                long sectoffset = offset;
                ResourceDirectory prd = this.getResourceDirectory();
                ByteBuffer resbuf = null; // TODO prd.buildResource(
                        // TODO sect.VirtualAddress);
                resbuf.position(0);

                out.write(resbuf);
                offset += resbuf.capacity();
                long rem = offset % this.header.FileAlignment;
                if (rem != 0) {
                    offset += this.header.FileAlignment - rem;
                }

                if (out.size() + 1 < offset) {
                    ByteBuffer padder = ByteBuffer.allocate(1);
                    out.write(padder, offset - 1);
                }

                long virtualSize = resbuf.capacity();
                if ((virtualSize % this.header.FileAlignment) > 0) {
                    virtualSize += this.header.SectionAlignment -
                            (virtualSize % this.header.SectionAlignment);
                }

                sect.PointerToRawData = sectoffset;
                sect.SizeOfRawData = resbuf.capacity();
                if ((sect.SizeOfRawData % this.header.FileAlignment) > 0) {
                    sect.SizeOfRawData += (this.header.FileAlignment -
                            (sect.SizeOfRawData
                            % this.header.FileAlignment));
                }
                sect.VirtualAddress = virtualAddress;
                sect.VirtualSize = virtualSize;

                virtualAddress += virtualSize;
            } else if (sect.PointerToRawData > 0) {
                //			System.out.println("Dumping section " + i + "/" + sect.getName() + " at " + offset + " from " + sect.PointerToRawData + " (VA=" + virtualAddress + ")");
                out.position(offset);
                this.channel.position(sect.PointerToRawData);
                long sectoffset = offset;

                out.position(offset + sect.SizeOfRawData);
                ByteBuffer padder = ByteBuffer.allocate(1);
                out.write(padder, offset + sect.SizeOfRawData - 1);

                long outted = out.transferFrom(this.channel, offset,
                        sect.SizeOfRawData);
                offset += sect.SizeOfRawData;

                long rem = offset % this.header.FileAlignment;
                if (rem != 0) {
                    offset += this.header.FileAlignment - rem;
                }

                // 			long virtualSize = sect.SizeOfRawData;
                // 			if ((virtualSize % peheader.SectionAlignment)>0)
                // 			    virtualSize += peheader.SectionAlignment - (virtualSize%peheader.SectionAlignment);

                sect.PointerToRawData = sectoffset;
                //			sect.SizeOfRawData =
                sect.VirtualAddress = virtualAddress;
                //			sect.VirtualSize = virtualSize;

                virtualAddress += sect.VirtualSize;
                if ((virtualAddress % this.header.SectionAlignment) > 0) {
                    virtualAddress += this.header.SectionAlignment -
                            (virtualAddress % this.header.SectionAlignment);
                }
            } else {
                // generally a BSS, with a virtual size but no
                // data in the file...
                long virtualSize = sect.VirtualSize;
                if ((virtualSize % this.header.SectionAlignment) > 0) {
                    virtualSize += this.header.SectionAlignment -
                            (virtualSize % this.header.SectionAlignment);
                }

                sect.VirtualAddress = virtualAddress;
                //			sect.VirtualSize = virtualSize;
                virtualAddress += virtualSize;
            }
        }

        // Now that all the sections have been written, we have the
        // correct VirtualAddress and Sizes, so we can update the new
        // header and all the section headers...
        this.header.updateVAAndSize(shs, shs);

        bb = this.header.getData();
        bb.position(0);
        out.position(oldMSDOSHeader.e_lfanew);
        outputcount = out.write(bb);

        offset = this.oldMSDOSHeader.e_lfanew +
                (header.NumberOfRvaAndSizes * 8) + 24 + 96;
        out.position(offset);
        for (int i = 0; i < shs.size(); i++) {
            SectionHeader h = shs.get(i);
            ByteBuffer buf = h.getData();
            outputcount = out.write(buf);
        }

        fos.flush();
        fos.close();
    }

    /**
     * Replaces default .exe icon.
     *
     * @param icon new icon
     * @throws Exception if no icon exist in the .exe
     */
    public void replaceDefaultIcon(ResIcon icon) throws Exception {
        ResourceDirectory resdir = getResourceDirectory();

        ResourceDataEntry entry = resdir.getData("#14", null, null);
        if (entry == null) {
            throw new Exception("Can't find any icon group in the file!");
        }

        entry.Data.position(0);
        entry.Data.position(0);

        ResIconDir rid = new ResIconDir(entry.Data);
        int iconid = rid.getEntries()[0].dwImageOffset;

        ResourceDataEntry iconentry = resdir.getData("#3", "#"
                + iconid, null);
        iconentry.Data.position(0);

        rid.getEntries()[0].bWidth = (short) icon.Width;
        rid.getEntries()[0].bHeight = (short) (icon.Height / 2);
        rid.getEntries()[0].bColorCount = (short) (1 << icon.BitsPerPixel);
        rid.getEntries()[0].wBitCount = icon.BitsPerPixel;
        rid.getEntries()[0].dwBytesInRes = icon.getData().remaining();

        iconentry.Data = icon.getData();
        iconentry.Size = iconentry.Data.remaining();

        entry.setData(rid.getData());
    }
}
