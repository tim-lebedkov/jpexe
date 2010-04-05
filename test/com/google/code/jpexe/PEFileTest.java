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
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests PEFile.
 */
public class PEFileTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void read7zip() throws IOException {
        PEFile pef = new PEFile(new File("test/7z.exe"));
        pef.open();
        pef.close();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    /*@Test
    public void addResString() throws IOException, CloneNotSupportedException {
        PEFile pef = new PEFile(new File("C:\\Dokumente und Einstellungen\\tim.TIMPC\\Eigene Dateien\\projects\\jliftoff\\demo\\SimpleApp\\SimpleApp.exe"));
        //PEFile pef = new PEFile(new File("C:\\Dokumente und Einstellungen\\tim.TIMPC\\Desktop\\ConsoleNB.exe"));
        pef.open();
        pef.getResourceDirectory().getRoot().dump(System.out, 0);
        /*
        ByteBuffer bb = ByteBuffer.allocate(5);
        bb.put((byte) '1');
        bb.put((byte) '.');
        bb.put((byte) '7');
        bb.put((byte) '.');
        bb.put((byte) '8');

        pef.getResourceDirectory().m_master.dump(System.out);
        pef.getResourceDirectory().dump(System.out);
        final ImageResourceDirectory root = pef.getResourceDirectory().getRoot();
        ResourceEntry catEntry = root.getResourceEntry(10);
        ResourceEntry identEntry = catEntry.Directory.getResourceEntry(2);
        ResourceEntry langEntry = identEntry.Directory.getResourceEntry(1024);
        langEntry.Data.setData(bb);
        pef.dumpTo(new File("C:\\Dokumente und Einstellungen\\tim.TIMPC\\Desktop\\ConsoleNB2.exe"));
         *
         *
        pef.close();
    }*/
}