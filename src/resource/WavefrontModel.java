/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import graphics.Material;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;
import physics.BoundingBox;
import physics.Mesh;

/**
 *
 * @author Andy
 */
public class WavefrontModel extends Resource implements physics.Boundable {

    String path;
    private String pathPrefix;
    private String fullName;
    private ArrayList<Vector3f> vertices;
    private ArrayList<Vector3f> normals;
    private ArrayList<Vector2f> texCoords;
    private ArrayList<graphics.Material> materialList;
    private ArrayList<Mesh> objects;
    BoundingBox b;

    public WavefrontModel(String path) {
        this("", path);
    }

    public WavefrontModel(String prefix, String path) {
        this.path = path;
        this.pathPrefix = prefix;
        this.fullName = this.pathPrefix + this.path;
        b = new BoundingBox();
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

    public ArrayList<Mesh> getObjects() {
        return objects;
    }

    @Override
    public boolean load() {
        vertices = new ArrayList<Vector3f>();
        normals = new ArrayList<Vector3f>();
        texCoords = new ArrayList<Vector2f>();
        materialList = new ArrayList<graphics.Material>();
        objects = new ArrayList<Mesh>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + this.fullName + ".obj")));
            HashMap<String, graphics.Material> materials = new HashMap<String, graphics.Material>();
            String line;
            graphics.Material currentMaterial = graphics.Material.defaultMaterial;
            Mesh currentMesh = new Mesh(this.fullName);
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                String[] spaceSplit = line.split(" ");
                if (line.startsWith("mtllib ")) {
                    String materialFileName = spaceSplit[1];
                    materials.putAll(loadMaterialLibrary(this.pathPrefix, materialFileName));
                } else if (line.startsWith("o ")) {
                    currentMesh = new Mesh(spaceSplit[1]);
                    addMesh(currentMesh);
                } else if (line.startsWith("usemtl ")) {
                    if (spaceSplit.length > 1) {
                        currentMaterial = materials.get(spaceSplit[1]);
                        if (currentMesh.getName().equals(this.fullName)) {
                            currentMesh = new Mesh(currentMaterial.getName());
                            addMesh(currentMesh);
                        }
                    } else {
                        currentMaterial = Material.defaultMaterial;
                        if (currentMesh.getName().equals(this.fullName)) {
                            currentMesh = new Mesh(this.fullName);
                            addMesh(currentMesh);
                        }
                    }
                    currentMesh.setMaterial(currentMaterial);
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
                    float u = Float.valueOf(xyz[1]);
                    float v = Float.valueOf(xyz[2]);
                    texCoords.add(new Vector2f(u, 1 - v));
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
                    currentMesh.addFace(new graphics.Face(vertexIndicesArray, normalIndicesArray, texCoordIndicesArray));
                } else if (line.startsWith("dim ")) {
                    float x, y, z;
                    x = Float.parseFloat(spaceSplit[1]);
                    y = Float.parseFloat(spaceSplit[2]);
                    z = Float.parseFloat(spaceSplit[3]);
                    b.setPosition(new Vector3f(0, 0, 0));
                    b.setDimension(new Vector3f(x, y, z));
                    b.create();
                }
            }
            if (b.getDimension().lengthSquared() == 0) {
                Vector3f[] yolo = new Vector3f[vertices.size()];
                vertices.toArray(yolo);
                BoundingBox fit = BoundingBox.boundsFromVerts(yolo);
                b.setDimension(fit.getDimension());
                b.setPosition(fit.getPosition());
                b.create();
            }
            reader.close();
            materialList = new ArrayList<graphics.Material>(materials.values());

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static HashMap<String, graphics.Material> loadMaterialLibrary(String pathPrefix, String materialFilePath) {
        HashMap<String, graphics.Material> materials = new HashMap<String, graphics.Material>();
        try {
            System.out.println("WHAT + " + materialFilePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + pathPrefix + materialFilePath)));
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
                    parseMaterial = new graphics.Material(materialName);
                } else if (line.startsWith("Ns ")) {
                    parseMaterial.setSpecularCoefficient(Float.valueOf(spaceSplit[1]));
                } else if (line.startsWith("Ka ")) {
                    parseMaterial.setAmbientColor(new float[]{Float.valueOf(spaceSplit[1]), Float.valueOf(spaceSplit[2]), Float.valueOf(spaceSplit[3])});
                } else if (line.startsWith("Ks ")) {
                    parseMaterial.setSpecularColor(new float[]{Float.valueOf(spaceSplit[1]), Float.valueOf(spaceSplit[2]), Float.valueOf(spaceSplit[3])});
                } else if (line.startsWith("Kd ")) {
                    parseMaterial.setDiffuseColor(new float[]{Float.valueOf(spaceSplit[1]), Float.valueOf(spaceSplit[2]), Float.valueOf(spaceSplit[3])});
                } else if (line.startsWith("map_Kd")) {
                    parseMaterial.setTexture(resource.TextureManager.getInstance().loadTextureResource(pathPrefix + spaceSplit[1]));
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

    public static HashMap<String, graphics.Material> loadMaterialLibrary(String materialFilePath) {
        return loadMaterialLibrary("", materialFilePath);
    }

    public String toString() {
        if (objects == null) {
            return getFullName();
        } else {
            return getFullName()
                    + "\nObjects: " + objects.size()
                    + "\nVertices: " + vertices.size()
                    + "\nNormals: " + normals.size()
                    + "\nTexture Coordinates: " + texCoords.size()
                    + "\nMaterials: " + materialList.size();
        }
    }

    @Override
    public BoundingBox getBounds() {
        return b;
    }

    private void addMesh(Mesh currentMesh) {
        objects.add(currentMesh);
        currentMesh.setVertices(vertices);
        currentMesh.setNormals(normals);
        currentMesh.setTexCoords(texCoords);
        currentMesh.setBounds(b);
    }

    public String getPath() {
        return path;
    }

    @Override
    public Vector3f getPosition() {
        return getBounds().getPosition();
    }
}
