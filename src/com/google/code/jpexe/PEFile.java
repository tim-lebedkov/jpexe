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
    private File m_file;
    private FileInputStream m_in = null;
    private FileChannel m_channel_ = null;

    private PEOldMSHeader m_oldmsheader;

    /**
     * Data between the old MS-DOS header (64 bytes long) and the new
     * PE header (starting with 'PE'\0\0). Contains also the MS-DOS 2.0 stub
     * program.
     */
    private SimpleBinaryRecord header2;

    public PEHeader m_header;

    private List<PESection> m_sections = new ArrayList<PESection>();
    private PEResourceDirectory m_resourceDir;

    /**
     * Creates a new instance of PEFile
     *
     * @param f an .exe
     */
    public PEFile(File f) {
        m_file = f;
    }

    public void close() throws IOException {
        m_in.close();
    }

    public void open() throws IOException {
        m_in = new FileInputStream(m_file);
        m_channel_ = m_in.getChannel();

        MappedByteBuffer mbb =
                m_channel_.map(MapMode.READ_ONLY, 0, m_channel_.size());
        mbb.order(ByteOrder.LITTLE_ENDIAN);

        // read the MS-DOS header (starts with 'MZ')
        m_oldmsheader = new PEOldMSHeader();
        m_oldmsheader.setData(mbb);

        // read everything between 2 headers (including the MS-DOS 2.0 stub)
        this.header2 = new SimpleBinaryRecord(m_oldmsheader.e_lfanew -
                mbb.position());
        this.header2.setData(mbb);

        // read PE header (starts with 'PE')
        m_header = new PEHeader();
        m_header.setData(mbb);

        int seccount = m_header.NumberOfSections;
        int headoffset = m_oldmsheader.e_lfanew;
        long offset = headoffset + (m_header.NumberOfRvaAndSizes * 8) + 24 + 96;

        for (int i = 0; i < seccount; i++) {
            PESection sect = new PESection(this, offset);
            sect.read();
            // sect.dump(System.out);
            m_sections.add(sect);
            offset += 40;
        }

        ByteBuffer resbuf = null;
        long resourceoffset = m_header.ResourceDirectory_VA;
        for (int i = 0; i < seccount; i++) {
            PESection sect = m_sections.get(i);
            if (sect.VirtualAddress == resourceoffset) {
                //			System.out.println("  Resource section found: " + resourceoffset);
                PEResourceDirectory prd = new PEResourceDirectory(this, sect);
                resbuf = prd.buildResource(sect.VirtualAddress);
                break;
            }
        }
    }

    public FileChannel getChannel() {
        return m_channel_;
    }

    public static void main(String args[]) throws IOException,
            CloneNotSupportedException, Exception {
        //(no)PEFile pe = new PEFile(new File("F:/Program Files/LAN Search PRO/lansearch.exe"));

        PEFile pe =
                new PEFile(
                new File(
                "F:/Documents and Settings/Rodrigo/Mes documents/projects/jsmooth/skeletons/simplewrap/JWrap.exe"));
        //PEFile pe = new PEFile(new File("c:/projects/jwrap/Copie.exe"));
        // PEFile pe = new PEFile(new File("c:/projects/jwrap/test.exe"));
        // PEFile pe = new PEFile(new File("F:/Program Files/bQuery/bQuery.exe"));
        // PEFile pe = new PEFile(new File("F:/Program Files/Server Query/query.exe"));
        // PEFile pe = new PEFile(new File("F:/Program Files/AvRack/rtlrack.exe"));
        pe.open();

        //	System.out.println("===============\nADDING A RES");
        File fout =
                new File(
                "F:/Documents and Settings/Rodrigo/Mes documents/projects/jsmooth/skeletons/simplewrap/gen-application.jar");
        FileInputStream fis = new FileInputStream(fout);

        ByteBuffer data = ByteBuffer.allocate((int) fout.length());
        data.order(ByteOrder.LITTLE_ENDIAN);
        FileChannel fischan = fis.getChannel();
        fischan.read(data);
        data.position(0);
        fis.close();

        PEResourceDirectory resdir = pe.getResourceDirectory();
        //	boolean resb = resdir.replaceResource("JAVA", 103, 1033, data);

        //	String mainclassname = "net.charabia.generation.application.Application";
        //	String mainclassname = "net/charabia/generation/application/Application";
        //	ByteBuffer bcn = ByteBuffer.allocate(mainclassname.length()+1);
        //	for (int i=0; i<mainclassname.length(); i++)
        //	{
        //		bcn.put( (byte) mainclassname.charAt(i));
        //	}
        //	bcn.put((byte)0);
        //	bcn.position(0);
        //	resb = resdir.replaceResource("JAVA", 102, 1033, bcn);

        //	PEResourceDirectory.DataEntry entry = resdir.getData("#14", "A", "#1033");
        //	entry.Data.position(0);
        //	System.out.println("DataEntry found : " + entry + " (size=" + entry.Data.remaining() + ")");
        //	entry.Data.position(0);
        //
        //	ResIconDir rid = new ResIconDir(entry.Data);
        //	System.out.println("ResIconDir :");
        //	System.out.println(rid.toString());
        //	int iconid = rid.getEntries()[0].dwImageOffset;
        //	System.out.println("Icon Index: " + iconid);
        //
        //	PEResourceDirectory.DataEntry iconentry = resdir.getData("#3", "#"+iconid, "#1033");
        //	iconentry.Data.position(0);
        //	ResIcon icon = new ResIcon(iconentry.Data);
        //	System.out.println("Icon :");
        //	System.out.println(icon.toString());

        //java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage ("c:\\test.gif");
        // java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage ("c:\\gnome-day2.png");
        java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(
                "c:\\gnome-color-browser2.png");

        java.awt.MediaTracker mt = new java.awt.MediaTracker(new javax.swing.JLabel(
                "toto"));
        mt.addImage(img, 1);
        try {
            mt.waitForAll();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        ResIcon newicon = new ResIcon(img);

        pe.replaceDefaultIcon(newicon);

        //	System.out.println("-----------------\nNEW ICON:");
        //	System.out.println(newicon.toString());
        //
        //	rid.getEntries()[0].bWidth = (short)newicon.Width;
        //	rid.getEntries()[0].bHeight = (short)(newicon.Height/2);
        //	rid.getEntries()[0].bColorCount = (short)(1 <<newicon.BitsPerPixel);
        //	rid.getEntries()[0].wBitCount = newicon.BitsPerPixel;
        //	rid.getEntries()[0].dwBytesInRes = newicon.getData().remaining();
        //
        //	iconentry.Data = newicon.getData();
        //	iconentry.Size = iconentry.Data.remaining();
        //
        //	entry.setData(rid.getData());
        //	System.out.println("POST CHANGE ResIconDir :");
        //	System.out.println(rid.toString());

        // ResIcon test = new ResIcon(icon.getData());
        // System.out.println("PROOF-TEST:\n" + test.toString());

        /// BACK
        //
        //	rid.getEntries()[0].bWidth = (short)icon.Width;
        //	rid.getEntries()[0].bHeight = (short)(icon.Height/2);
        //	rid.getEntries()[0].bColorCount = (short)(1 <<icon.BitsPerPixel);
        //	rid.getEntries()[0].wBitCount = icon.BitsPerPixel;
        //	iconentry.Data = icon.getData();
        //	iconentry.Size = iconentry.Data.remaining();

        // resdir.addNewResource("POUET", "A666", "#1033", data);


        //resdir.dump(System.out);


        //	System.out.println("New size = " + resdir.size());
        File out =
                new File(
                "F:/Documents and Settings/Rodrigo/Mes documents/projects/jsmooth/skeletons/simplewrap/COPIE.exe");
        pe.dumpTo(out);


    }

    public PEResourceDirectory getResourceDirectory() throws IOException {
        if (m_resourceDir != null) {
            return m_resourceDir;
        }

        long resourceoffset = m_header.ResourceDirectory_VA;
        for (int i = 0; i < m_sections.size(); i++) {
            PESection sect = m_sections.get(i);
            if (sect.VirtualAddress == resourceoffset) {
                m_resourceDir = new PEResourceDirectory(this, sect);
                return m_resourceDir;
            }
        }

        return null;
    }

    /**
     * Adds a new section
     *
     * @param s new section. VirtualAddress will be set automatically.
     */
    public void addSection(PESection s) {
        long va = -1;
        for (PESection s2: this.m_sections) {
            if (s2.VirtualAddress > va) {
                va = s2.VirtualAddress;
                s.VirtualAddress = s2.VirtualAddress + s2.VirtualSize;
            }
        }

        this.m_sections.add(s);
        this.m_header.NumberOfSections = this.m_sections.size();
    }

    public void dumpTo(File destination) throws IOException,
            CloneNotSupportedException {
        int outputcount = 0;
        FileOutputStream fos = new FileOutputStream(destination);
        FileChannel out = fos.getChannel();

        // Make a copy of the Header, for safe modifications
        List<PESection> sections = new ArrayList<PESection>();
        for (int i = 0; i < m_sections.size(); i++) {
            PESection sect = m_sections.get(i);
            PESection cs = (PESection) sect.clone();
            sections.add(cs);
        }

        // First, write the old MS Header, the one starting
        // with "MZ"...
        ByteBuffer bb = this.m_oldmsheader.getData();
        bb.position(0);
        outputcount = out.write(bb);

        // write everything between 2 headers
        bb = this.header2.getData();
        bb.position(0);
        out.write(bb);

        // Then Write the new Header...
        bb = this.m_header.getData();
        bb.position(0);
        out.write(bb);

        // After the header, there are all the section
        // headers...
        long offset = this.m_oldmsheader.e_lfanew +
                (m_header.NumberOfRvaAndSizes * 8)
                + 24 + 96;
        out.position(offset);
        for (int i = 0; i < sections.size(); i++) {
            // System.out.println("  offset: " + out.position());
            PESection sect = sections.get(i);

            ByteBuffer buf = sect.get();
            outputcount = out.write(buf);
        }

        // Now, we write the real data: each of the section
        // and their data...

        // Not sure why it's always at 1024... ?
        offset = 1024;

        long virtualAddress = offset;
        if ((virtualAddress % this.m_header.SectionAlignment) > 0) {
                virtualAddress += this.m_header.SectionAlignment -
                (virtualAddress % this.m_header.SectionAlignment);
        }

        // Dump each section data
        long resourceoffset = m_header.ResourceDirectory_VA;
        for (int i = 0; i < sections.size(); i++) {
            PESection sect = sections.get(i);
            if (resourceoffset == sect.VirtualAddress) {
                // System.out.println("Dumping RES section " + i + " at " + offset + " from " + sect.PointerToRawData + " (VA=" + virtualAddress + ")");
                out.position(offset);
                long sectoffset = offset;
                PEResourceDirectory prd = this.getResourceDirectory();
                ByteBuffer resbuf = prd.buildResource(sect.VirtualAddress);
                resbuf.position(0);

                out.write(resbuf);
                offset += resbuf.capacity();
                long rem = offset % this.m_header.FileAlignment;
                if (rem != 0) {
                    offset += this.m_header.FileAlignment - rem;
                }

                if (out.size() + 1 < offset) {
                    ByteBuffer padder = ByteBuffer.allocate(1);
                    out.write(padder, offset - 1);
                }

                long virtualSize = resbuf.capacity();
                if ((virtualSize % this.m_header.FileAlignment) > 0) {
                    virtualSize += this.m_header.SectionAlignment -
                            (virtualSize % this.m_header.SectionAlignment);
                }

                sect.PointerToRawData = sectoffset;
                sect.SizeOfRawData = resbuf.capacity();
                if ((sect.SizeOfRawData % this.m_header.FileAlignment) > 0) {
                    sect.SizeOfRawData += (this.m_header.FileAlignment -
                            (sect.SizeOfRawData
                            % this.m_header.FileAlignment));
                }
                sect.VirtualAddress = virtualAddress;
                sect.VirtualSize = virtualSize;

                virtualAddress += virtualSize;
            } else if (sect.PointerToRawData > 0) {
                //			System.out.println("Dumping section " + i + "/" + sect.getName() + " at " + offset + " from " + sect.PointerToRawData + " (VA=" + virtualAddress + ")");
                out.position(offset);
                this.m_channel_.position(sect.PointerToRawData);
                long sectoffset = offset;

                out.position(offset + sect.SizeOfRawData);
                ByteBuffer padder = ByteBuffer.allocate(1);
                out.write(padder, offset + sect.SizeOfRawData - 1);

                long outted = out.transferFrom(this.m_channel_, offset,
                        sect.SizeOfRawData);
                offset += sect.SizeOfRawData;
                //			System.out.println("offset before alignment, " + offset);

                long rem = offset % this.m_header.FileAlignment;
                if (rem != 0) {
                    offset += this.m_header.FileAlignment - rem;
                }
                //			System.out.println("offset after alignment, " + offset);

                // 			long virtualSize = sect.SizeOfRawData;
                // 			if ((virtualSize % peheader.SectionAlignment)>0)
                // 			    virtualSize += peheader.SectionAlignment - (virtualSize%peheader.SectionAlignment);

                sect.PointerToRawData = sectoffset;
                //			sect.SizeOfRawData =
                sect.VirtualAddress = virtualAddress;
                //			sect.VirtualSize = virtualSize;

                virtualAddress += sect.VirtualSize;
                if ((virtualAddress % this.m_header.SectionAlignment) > 0) {
                    virtualAddress += this.m_header.SectionAlignment -
                            (virtualAddress % this.m_header.SectionAlignment);
                }
            } else {
                // generally a BSS, with a virtual size but no
                // data in the file...
                //			System.out.println("Dumping section " + i + " at " + offset + " from " + sect.PointerToRawData + " (VA=" + virtualAddress + ")");
                long virtualSize = sect.VirtualSize;
                if ((virtualSize % this.m_header.SectionAlignment) > 0) {
                    virtualSize += this.m_header.SectionAlignment -
                            (virtualSize % this.m_header.SectionAlignment);
                }

                sect.VirtualAddress = virtualAddress;
                //			sect.VirtualSize = virtualSize;
                virtualAddress += virtualSize;
            }
        }

        // Now that all the sections have been written, we have the
        // correct VirtualAddress and Sizes, so we can update the new
        // header and all the section headers...

        this.m_header.updateVAAndSize(m_sections, sections);

        bb = this.m_header.getData();
        bb.position(0);
        out.position(m_oldmsheader.e_lfanew);
        outputcount = out.write(bb);

        // peheader.dump(System.out);
        ///	System.out.println("Dumping the section again...");
        offset = this.m_oldmsheader.e_lfanew +
                (m_header.NumberOfRvaAndSizes * 8) + 24 + 96;
        out.position(offset);
        for (int i = 0; i < sections.size(); i++) {
            //		System.out.println("  offset: " + out.position());
            PESection sect = sections.get(i);
            // sect.dump(System.out);
            ByteBuffer buf = sect.get();
            outputcount = out.write(buf);
        }

        fos.flush();
        fos.close();
    }

    /*
     */
    public void replaceDefaultIcon(ResIcon icon) throws Exception {
        PEResourceDirectory resdir = getResourceDirectory();

        PEResourceDirectory.DataEntry entry = resdir.getData("#14", null, null);
        if (entry == null) {
            throw new Exception("Can't find any icon group in the file!");
        }

        entry.Data.position(0);
        //	System.out.println("DataEntry found : " + entry + " (size=" + entry.Data.remaining() + ")");
        entry.Data.position(0);

        ResIconDir rid = new ResIconDir(entry.Data);
        //	System.out.println("ResIconDir :");
        //	System.out.println(rid.toString());
        int iconid = rid.getEntries()[0].dwImageOffset;
        //	System.out.println("Icon Index: " + iconid);

        PEResourceDirectory.DataEntry iconentry = resdir.getData("#3", "#"
                + iconid, null);
        iconentry.Data.position(0);
        //	System.out.println("Icon :");
        //	System.out.println(icon.toString());

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
