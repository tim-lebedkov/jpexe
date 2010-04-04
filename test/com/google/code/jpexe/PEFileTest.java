package com.google.code.jpexe;


import com.google.code.jpexe.PEResourceDirectory.ImageResourceDirectory;
import com.google.code.jpexe.PEResourceDirectory.ResourceEntry;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author tim
 */
public class PEFileTest {

    public PEFileTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void addResString() throws IOException, CloneNotSupportedException {
        PEFile pef = new PEFile(new File("C:\\Dokumente und Einstellungen\\tim.TIMPC\\Desktop\\ConsoleNB.exe"));
        pef.open();
        // pef.getResourceDirectory().getRoot().dump(System.out, 0);
        ByteBuffer bb = ByteBuffer.allocate(5);
        bb.put((byte) '1');
        bb.put((byte) '.');
        bb.put((byte) '7');
        bb.put((byte) '.');
        bb.put((byte) '8');

        final ImageResourceDirectory root = pef.getResourceDirectory().getRoot();
        ResourceEntry catEntry = root.getResourceEntry(10);
        ResourceEntry identEntry = catEntry.Directory.getResourceEntry(2);
        ResourceEntry langEntry = identEntry.Directory.getResourceEntry(1024);
        langEntry.Data.setData(bb);
        pef.dumpTo(new File("C:\\Dokumente und Einstellungen\\tim.TIMPC\\Desktop\\ConsoleNB2.exe"));
        pef.close();
    }
}