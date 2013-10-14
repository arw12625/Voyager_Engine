/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import graphics.Face;
import graphics.Material;
import java.util.ArrayList;
import org.lwjgl.util.vector.*;

/**
 *
 * @author Andy
 */
public class Mesh extends game.GameObject implements Boundable {

    String name;
    private ArrayList<Vector3f> vertices;
    private ArrayList<Vector3f> normals;
    private ArrayList<Vector2f> texCoords;
    private ArrayList<graphics.Face> faces;
    private Material m;
    BoundingBox b;

    public Mesh(String name) {
        this.name = name;
        faces = new ArrayList<Face>();
    }

    public void setVertices(ArrayList<Vector3f> vertices) {
        this.vertices = vertices;
    }

    public void setNormals(ArrayList<Vector3f> normals) {
        this.normals = normals;
    }

    public void setTexCoords(ArrayList<Vector2f> texCoords) {
        this.texCoords = texCoords;
    }

    public void setMaterial(Material material) {
        this.m = material;
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

    public void addFace(Face f) {
        faces.add(f);
    }

    public String toString() {
        return getFullName() + name
                + "\nFaces: " + faces.size()
                + "\nMaterial: " + m;
    }

    @Override
    public BoundingBox getBounds() {
        return b;
    }

    public void setBounds(BoundingBox b) {
        this.b = b;
    }

    public Object getName() {
        return name;
    }

    public Material getMaterial() {
        return m;
    }

    public boolean hasNormals() {
        return normals != null && normals.size() > 0;
    }
    
    public boolean hasTexCoords() {
        return texCoords != null && texCoords.size() > 0;
    }
}
