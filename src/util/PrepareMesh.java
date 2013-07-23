/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import resource.ResourceManager;
import physics.Mesh;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;
import physics.BoundingBox;

/**
 *
 * @author Andy
 */
public class PrepareMesh {

    public static void main(String[] args) {
        prepareMesh("planet-orig", "planet");
    }

    public static void prepareMesh(String origPath, String fixPath) {
        Mesh m = new Mesh(origPath);
        m.load();
        Vector3f[] verts = new Vector3f[m.getVertices().size()];
        for(int i = 0; i < verts.length; i++) {
            verts[i] = m.getVertices().get(i);
        }
        BoundingBox b = BoundingBox.boundsFromVerts(verts);
        Vector3f offset = Vector3f.sub(b.getMax(), b.getHalf(), null);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + origPath + ".obj")));
            String line;
            int i = 0;
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    new File("res/" + fixPath + ".obj").getAbsoluteFile()));
            writer.write("dim " + vecToString(b.getDimension()) + "\n");
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    writer.write("v " + vecToString(Vector3f.sub(verts[i++], offset, null)));
                } else {
                    writer.write(line);
                }
                writer.write("\n");
            }
            reader.close();
            writer.close();
            FileChannel in = new FileInputStream(new File("res/" + origPath + ".mtl")).getChannel();
            FileChannel out = new FileOutputStream(new File("res/" + fixPath + ".mtl")).getChannel();
            out.transferFrom(in, 0, in.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String vecToString(Vector3f v) {
        StringBuilder s = new StringBuilder();
        s.append(v.getX());
        s.append(" ");
        s.append(v.getY());
        s.append(" ");
        s.append(v.getZ());
        return s.toString();
    }
}
