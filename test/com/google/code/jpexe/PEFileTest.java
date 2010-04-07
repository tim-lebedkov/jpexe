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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests PEFile.
 */
public class PEFileTest {
    @Test
    public void read7zip() throws IOException {
        PEFile pef = new PEFile(new File("test/7z.exe"));
        pef.open();
        pef.close();
    }

    @Test
    public void read7zFM() throws IOException {
        PEFile pef = new PEFile(new File("test/7zFM.exe"));
        pef.open();
        pef.close();
    }

    @Test
    public void resourceDirectory() throws IOException {
        PEFile pef = new PEFile(new File("test/7zFM.exe"));
        pef.open();
        assertNotNull(pef.getResourceDirectory());
        pef.close();
    }

    @Test
    public void countSections() throws IOException {
        PEFile pef = new PEFile(new File("test/7z.exe"));
        pef.open();
        assertEquals(4, pef.getSections().size());
        pef.close();
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
}