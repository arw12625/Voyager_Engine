/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.GameObject;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import physics.Boundable;
import physics.BoundingBox;
import physics.Mesh;
import resource.WavefrontModel;

/**
 *
 * @author Andy
 */
public class ThreeDModel extends resource.GraphicsResource implements ThreeD, Boundable {

    WavefrontModel m;
    int[] vboVertexHandle;
    int[] vboNormalHandle;
    int[] vboTexCoordHandle;
    int[] amountOfVertices;
    Material[] materials;

    public ThreeDModel(WavefrontModel m) {
        this.m = m;
    }

    @Override
    protected boolean load() {
        return true;
    }
    
    @Override
    public boolean processGraphics() {
        int numberOfObjects = m.getObjects().size();
        vboVertexHandle = new int[numberOfObjects];
        vboNormalHandle = new int[numberOfObjects];
        vboTexCoordHandle = new int[numberOfObjects];
        amountOfVertices = new int[numberOfObjects];
        materials = new Material[numberOfObjects];
        for (int i = 0; i < numberOfObjects; i++) {
            Mesh mesh = m.getObjects().get(i);
            vboVertexHandle[i] = glGenBuffers();
            vboNormalHandle[i] = mesh.hasNormals() ? glGenBuffers() : -1;
            vboTexCoordHandle[i] = mesh.hasTexCoords() ? glGenBuffers() : -1;
            amountOfVertices[i] = mesh.getFaces().size() * 3;
            FloatBuffer vertices = BufferUtils.createFloatBuffer(amountOfVertices[i] * 3);
            FloatBuffer normals = null;
            if (mesh.hasNormals()) {
                normals = BufferUtils.createFloatBuffer(amountOfVertices[i] * 3);
            }
            FloatBuffer texCoords = null;
            if (mesh.hasTexCoords()) {
                texCoords = BufferUtils.createFloatBuffer(amountOfVertices[i] * 2);
            }
            for (Face face : mesh.getFaces()) {
                for (int j = 0; j < 3; j++) {
                    vertices.put(asFloats(m.getVertices().get(face.getVertexIndices()[j])));
                    if (mesh.hasNormals()) {
                        normals.put(asFloats(m.getNormals().get(face.getNormalIndices()[j])));
                    }
            
                    if (mesh.hasTexCoords()) {
                        texCoords.put(asFloats(m.getTexCoords().get(face.getTextureCoordinateIndices()[j])));
                    }
                }
            }
            materials[i] = mesh.getMaterial();
            vertices.flip();
            if (mesh.hasNormals()) {
                normals.flip();
            }
            if (mesh.hasTexCoords()) {
                texCoords.flip();
            }
            glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle[i]);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            if (mesh.hasNormals()) {
                glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle[i]);
                glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
            }
            if (mesh.hasTexCoords()) {
                glBindBuffer(GL_ARRAY_BUFFER, vboTexCoordHandle[i]);
                glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
            }
        }
        return true;
    }

    @Override
    public void render() {
        for (int i = 0; i < m.getObjects().size(); i++) {

            glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle[i]);
            glVertexPointer(3, GL_FLOAT, 0, 0L);

            if (vboNormalHandle[i] != -1) {
                glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle[i]);
                glNormalPointer(GL_FLOAT, 0, 0);
            }

            org.newdawn.slick.Color.white.bind();
            if (vboTexCoordHandle[i] != -1) {
                glBindBuffer(GL_ARRAY_BUFFER, vboTexCoordHandle[i]);
                glTexCoordPointer(2, GL_FLOAT, 0, 0);
                if (materials[i].isTextured()) {
                    materials[i].getTexture().bind();
                }
            }

            glBindBuffer(GL_ARRAY_BUFFER, 0);

            if (materials[i].getDiffuseColor() != null) {
                float[] col = materials[i].getDiffuseColor();
                glColor3f(col[0], col[1], col[2]);
            }

            glEnableClientState(GL_VERTEX_ARRAY);
            if (vboTexCoordHandle[i] != -1) {
                glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            }
            if (vboNormalHandle[i] != -1) {
                glEnableClientState(GL_NORMAL_ARRAY);
            }

            glDrawArrays(GL_TRIANGLES, 0, amountOfVertices[i]);

            if (vboTexCoordHandle[i] != -1) {
                glDisableClientState(GL_TEXTURE_COORD_ARRAY);
            }
            if (vboNormalHandle[i] != -1) {
                glDisableClientState(GL_NORMAL_ARRAY);
            }
            glDisableClientState(GL_VERTEX_ARRAY);

            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }

    public float[] asFloats(Vector3f v) {
        return new float[]{v.getX(), v.getY(), v.getZ()};
    }

    public float[] asFloats(Vector2f v) {
        return new float[]{v.getX(), v.getY()};
    }

    private void myVertex3f(Vector3f v) {
        glVertex3f(v.getX(), v.getY(), v.getZ());
    }

    @Override
    public BoundingBox getBounds() {
        return m.getBounds();
    }

}
