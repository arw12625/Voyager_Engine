/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.util.vector.*;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Andy
 */
public class Mesh extends resource.Resource implements Boundable {

    String path;
    private ArrayList<Vector3f> vertices;
    private ArrayList<Vector3f> normals;
    private ArrayList<Vector2f> texCoords;
    private ArrayList<graphics.Face> faces;
    private ArrayList<graphics.Material> materialList;
    BoundingBox b;

    public Mesh(String path) {
        this.path = path;
    }

    @Override
    public void create() {
        super.create();
    }
    
    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public ArrayList<Vector3f> getNormals() {
        return normals;
    }

    public ArrayList<Vector2f> getTexCoords() {
        return texCoords;
    }

    public ArrayList<graphics.Face> getFaces() {
        return faces;
    }

    @Override
    public boolean load() {
        vertices = new ArrayList<Vector3f>();
        normals = new ArrayList<Vector3f>();
        texCoords = new ArrayList<Vector2f>();
        faces = new ArrayList<graphics.Face>();
        materialList = new ArrayList<graphics.Material>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + path + ".obj")));
            HashMap<String, graphics.Material> materials = new HashMap<String, graphics.Material>();
            String line;
            graphics.Material currentMaterial = graphics.Material.defaultMaterial;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                String[] spaceSplit = line.split(" ");
                if (line.startsWith("mtllib ")) {
                    String materialFileName = spaceSplit[1];
                    materials.putAll(loadMaterialLibrary(materialFileName));
                } else if (line.startsWith("usemtl ")) {
                    currentMaterial = materials.get(spaceSplit[1]);
                } else if (line.startsWith("v ")) {
                    String[] xyz = spaceSplit;
                    float x = Float.valueOf(xyz[1]);
                    float y = Float.valueOf(xyz[2]);
                    float z = Float.valueOf(xyz[3]);
                    vertices.add(new Vector3f(x, y, z));
                } else if (line.startsWith("vn ")) {
                    String[] xyz = spaceSplit;
                    float x = Float.valueOf(xyz[1]);
                    float y = Float.valueOf(xyz[2]);
                    float z = Float.valueOf(xyz[3]);
                    normals.add(new Vector3f(x, y, z));
                } else if (line.startsWith("vt ")) {
                    String[] xyz = spaceSplit;
                    float s = Float.valueOf(xyz[1]);
                    float t = Float.valueOf(xyz[2]);
                    texCoords.add(new Vector2f(s, t));
                } else if (line.startsWith("f ")) {
                    String[] faceIndices = spaceSplit;
                    int[] vertexIndicesArray = {Integer.parseInt(faceIndices[1].split("/")[0]) - 1,
                        Integer.parseInt(faceIndices[2].split("/")[0]) - 1, Integer.parseInt(faceIndices[3].split("/")[0]) - 1};
                    int[] normalIndicesArray = {Integer.parseInt(faceIndices[1].split("/")[2]) - 1,
                        Integer.parseInt(faceIndices[2].split("/")[2]) - 1, Integer.parseInt(faceIndices[3].split("/")[2]) - 1};
                    int[] texCoordIndicesArray = null;
                    if (currentMaterial.isTextured()) {
                        texCoordIndicesArray = new int[]{Integer.parseInt(faceIndices[1].split("/")[1]) - 1,
                            Integer.parseInt(faceIndices[2].split("/")[1]) - 1, Integer.parseInt(faceIndices[3].split("/")[1]) - 1};
                    }
                    faces.add(new graphics.Face(vertexIndicesArray, normalIndicesArray, texCoordIndicesArray, currentMaterial));
                } else if (line.startsWith("dim ")) {
                    float x, y, z;
                    x = Float.parseFloat(spaceSplit[1]);
                    y = Float.parseFloat(spaceSplit[2]);
                    z = Float.parseFloat(spaceSplit[3]);
                    b = new BoundingBox(new Vector3f(0, 0, 0), new Vector3f(x, y, z));
                    b.create();
                }
            }
            reader.close();
            materialList = new ArrayList<graphics.Material>(materials.values());

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static HashMap<String, graphics.Material> loadMaterialLibrary(String materialFilePath) {
        HashMap<String, graphics.Material> materials = new HashMap<String, graphics.Material>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + materialFilePath)));
            String line;
            graphics.Material parseMaterial = null;
            String materialName = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                String[] spaceSplit = line.split(" ");
                if (line.startsWith("newmtl ")) {
                    if (!materialName.equals("")) {
                        materials.put(materialName, parseMaterial);
                    }
                    materialName = spaceSplit[1];
                    parseMaterial = new graphics.Material();
                } else if (line.startsWith("Ns ")) {
                    parseMaterial.setSpecularCoefficient(Float.valueOf(spaceSplit[1]));
                } else if (line.startsWith("Ka ")) {
                    parseMaterial.setAmbientColor(new float[]{Float.valueOf(spaceSplit[1]), Float.valueOf(spaceSplit[2]), Float.valueOf(spaceSplit[3])});
                } else if (line.startsWith("Ks ")) {
                    parseMaterial.setSpecularColor(new float[]{Float.valueOf(spaceSplit[1]), Float.valueOf(spaceSplit[2]), Float.valueOf(spaceSplit[3])});
                } else if (line.startsWith("Kd ")) {
                    parseMaterial.setDiffuseColor(new float[]{Float.valueOf(spaceSplit[1]), Float.valueOf(spaceSplit[2]), Float.valueOf(spaceSplit[3])});
                } else if (line.startsWith("map_Kd")) {
                    parseMaterial.setTexture(resource.TextureManager.getInstance().loadTextureResource(spaceSplit[1]));
                } else {
                    util.DebugMessages.getInstance().write("[MTL] Unknown Line: " + line);
                }
            }
            materials.put(materialName, parseMaterial);
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return materials;
    }

    public String toString() {
        return getFullName()
                + "\nFaces: " + faces.size()
                + "\nVertices: " + vertices.size()
                + "\nNormals: " + normals.size()
                + "\nTexture Coordinates: " + texCoords.size()
                + "\nMaterials: " + materialList.size();
    }

    @Override
    public BoundingBox getBounds() {
        return b;
    }
}
