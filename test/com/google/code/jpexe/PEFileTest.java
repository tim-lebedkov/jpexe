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
    public void countSections() throws IOException {
        PEFile pef = new PEFile(new File("test/7z.exe"));
        pef.open();
        assertEquals(4, pef.getSections().size());
        pef.close();
    }
}