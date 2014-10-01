/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import game.Game;
import game.GameObjectManager;
import graphics.ThreeDGraphicsManager;
import input.InputManager;
import java.io.*;
import java.nio.channels.FileChannel;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;
import resource.ResourceManager;
import resource.TextureManager;
import resource.WavefrontModel;
import script.ScriptManager;
import update.UpdateManager;

/**
 *
 * @author Andy
 */
public class CenterMesh {

    public static void main(String[] args) {
        UpdateManager updateManager = UpdateManager.getInstance();
        ThreeDGraphicsManager graphicsManager = ThreeDGraphicsManager.getInstance();
        InputManager inputManager = InputManager.getInstance();
        ResourceManager resourceManager = ResourceManager.getInstance();
        GameObjectManager gameObjectManager = GameObjectManager.getInstance();
        ScriptManager scriptManager = ScriptManager.getInstance();

        Game.create("THE GAME", updateManager, graphicsManager, inputManager, resourceManager, gameObjectManager, scriptManager);

        TextureManager.getInstance().create();
        
        if (args.length == 0) {
            prepareMesh("agg8", "agg8_fix");
        } else if(args.length == 1) {
            prepareMesh(args[0], args[0] + "_fix");
        } else if(args.length == 2) {
            prepareMesh(args[0], args[1]);
        }
        
    }

    public static void prepareMesh(String origPath, String fixPath) {
        WavefrontModel m = new WavefrontModel(origPath);
        m.load();
        Vector3f[] verts = new Vector3f[m.getVertices().size()];
        for (int i = 0; i < verts.length; i++) {
            verts[i] = m.getVertices().get(i);
        }
        physics.BoundingBox b = boundsFromVerts(verts);
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
                    writer.write("v " + vecToString(Vector3f.sub(verts[i++], b.getPosition(), null)));
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

    public static physics.BoundingBox boundsFromVerts(Vector3f... verts) {
        Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3f max = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        for (Vector3f v : verts) {
            if (v.getX() > max.getX()) {
                max.setX(v.getX());
            }
            if (v.getY() > max.getY()) {
                max.setY(v.getY());
            }
            if (v.getZ() > max.getZ()) {
                max.setZ(v.getZ());
            }
            if (v.getX() < min.getX()) {
                min.setX(v.getX());
            }
            if (v.getY() < min.getY()) {
                min.setY(v.getY());
            }
            if (v.getZ() < min.getZ()) {
                min.setZ(v.getZ());
            }
        }

        Vector3f dim = Vector3f.sub(max, min, null);
        Vector3f half = new Vector3f(dim);
        half.scale(0.5f);
        Vector3f pos = Vector3f.add(min, half, null);
        physics.BoundingBox b = new physics.BoundingBox(pos, dim);
        return b;
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
