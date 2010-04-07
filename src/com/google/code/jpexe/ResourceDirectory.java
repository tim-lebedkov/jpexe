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

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List of resources.
 */
public class ResourceDirectory implements BinaryRecord {
    private long location;

    long Characteristics; // uint32_t
    public long TimeDateStamp; // uint32_t
    int MajorVersion; // uint16_t
    int MinorVersion; // uint16_t
    // int NumberOfNamedEntries; // uint16_t
    // int NumberOfIdEntries; // uint16_t

    List<ResourceEntry> NamedEntries =
            new ArrayList<ResourceEntry>();
    List<ResourceEntry> IdEntries =
            new ArrayList<ResourceEntry>();

    public void addNamedEntry(ResourceEntry entry) {
        this.NamedEntries.add(entry);
    }

    public void addIdEntry(ResourceEntry entry) {
        this.IdEntries.add(entry);
    }

    public void addEntry(ResourceEntry entry) {
        if (entry.Name != null) {
            addNamedEntry(entry);
        } else {
            addIdEntry(entry);
        }
    }

    public void dump(PrintStream out, int level) {
        indent(level, out);
        out.println("Directory: ");
        indent(level, out);
        out.println("Characteristics=" + this.Characteristics);
        indent(level, out);
        out.println("TimeDateStamp=" + this.TimeDateStamp);
        indent(level, out);
        out.println("MajorVersion=" + this.MajorVersion);
        indent(level, out);
        out.println("MinorVersion=" + this.MinorVersion);
        indent(level, out);
        out.println("NumberOfNamedEntries=" + this.NamedEntries.size());
        indent(level, out);
        out.println("NumberOfIdEntries=" + this.IdEntries.size());
        indent(level, out);
        out.println("Named Entries:");
        for (int i = 0; i < NamedEntries.size();
                i++) {
            ResourceEntry re = NamedEntries.get(i);
            re.dump(out, level + 1);
        }
        indent(level, out);
        out.println("Id Entries:");
        for (int i = 0; i < IdEntries.size();
                i++) {
            ResourceEntry re = IdEntries.get(i);
            re.dump(out, level + 1);
        }
    }

    private void indent(int level, PrintStream out) {
        for (int i = 0; i < level;
                i++) {
            out.print("    ");
        }
    }

    public int diskSize() {
        int size = 16;
        for (int i = 0; i < this.NamedEntries.size();
                i++) {
            ResourceEntry re = NamedEntries.get(i);
            size += re.diskSize();
        }
        for (int i = 0; i < this.IdEntries.size();
                i++) {
            ResourceEntry re = IdEntries.get(i);
            size += re.diskSize();
        }
        if ((size % 4) > 0) {
            size += 4 - (size % 4);
        }
        return size;
    }

    public ResourceEntry getResourceEntry(String name) {
        // If name == null, get the first entry in lexical
        // order. If no entry in lexical order, choose the
        // lowest integer id entry.
        if (name == null) {
            if (NamedEntries.size() > 0) {
                return NamedEntries.get(0);
            }
            if (IdEntries.size() > 0) {
                return IdEntries.get(0);
            }
            return null;
        }
        if ((name.length() > 0) && (name.charAt(0) == '#')) {
            try {
                String nb = name.substring(1);
                int i = Integer.parseInt(nb);
                return getResourceEntry(i);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        for (Iterator<ResourceEntry> i =
                this.NamedEntries.iterator(); i.hasNext();) {
            ResourceEntry re = i.next();
            if (name.equals(re.Name)) {
                return re;
            }
        }
        return null;
    }

    public ResourceEntry getResourceEntry(int id) {
        for (Iterator<ResourceEntry> i =
                this.IdEntries.iterator(); i.hasNext();) {
            ResourceEntry re = i.next();
            if (id == re.Id) {
                return re;
            }
        }
        return null;
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public ByteBuffer getData() {
        long virtualBaseOffset = 100; // TODO
        ByteBuffer buffer = ByteBuffer.allocate(100); // TODO
        //			System.out.println("Building Directory Entry buffer @ " + buffer.position());
        buffer.putInt((int) this.Characteristics);
        buffer.putInt((int) this.TimeDateStamp);
        buffer.putShort((short) this.MajorVersion);
        buffer.putShort((short) this.MinorVersion);
        buffer.putShort((short) this.NamedEntries.size());
        buffer.putShort((short) this.IdEntries.size());
        int dataOffset =
                buffer.position() + (NamedEntries.size() * 8) +
                (IdEntries.size() * 8);
        for (int i = 0; i < this.NamedEntries.size();
                i++) {
            ResourceEntry re = this.NamedEntries.get(i);
            // TODO dataOffset = re.buildBuffer(buffer, virtualBaseOffset, dataOffset);
        }
        for (int i = 0; i < this.IdEntries.size();
                i++) {
            ResourceEntry re = this.IdEntries.get(i);
            // TODO dataOffset = re.buildBuffer(buffer, virtualBaseOffset, dataOffset);
        }
        buffer.position(dataOffset);
        return buffer;
    }

    public void setData(ByteBuffer header) {
        Characteristics = header.getInt();
        TimeDateStamp = header.getInt();
        MajorVersion = header.getShort();
        MinorVersion = header.getShort();
        short NumberOfNamedEntries = header.getShort();
        short NumberOfIdEntries = header.getShort();
        for (int i = 0; i < NumberOfNamedEntries;
                i++) {
            // TODO ResourceEntry re = new ResourceEntry(header);
            // TODO NamedEntries.add(re);
        }
        for (int i = 0; i < NumberOfIdEntries;
                i++) {
            // TODO ResourceEntry re = new ResourceEntry(header);
            // TODO IdEntries.add(re);
        }
    }
    
    /**
     * Returns an entry with the specified ID. Creates a new entry if it
     * does not exist
     *
     * @param id ID of the entry
     * @return entry with the specified ID (without any data or
     *     subdirectory)
     */
    /* todo
    public ResourceEntry getOrCreateResourceEntry(int id) {
    ResourceEntry r = getResourceEntry(id);
    if (r == null) {
    r = buildResourceEntry(id, (DataEntry) null);
    addEntry(r);
    }
    return r;
    }*/
    /**
     * Returns an entry with the specified ID. Creates a new entry if it
     * does not exist
     *
     * @param id ID of the entry
     * @return entry with the specified ID (without any data or
     *     subdirectory)
     */
    /* todo
    public ResourceEntry getOrCreateResourceEntry(int id) {
    ResourceEntry r = getResourceEntry(id);
    if (r == null) {
    r = buildResourceEntry(id, (DataEntry) null);
    addEntry(r);
    }
    return r;
    }*/
}
