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

/**
 *
 * @author Andy
 */
public class ThreeDModel extends GameObject implements ThreeD {

    physics.Mesh m;
    int vboVertexHandle;
    int vboNormalHandle;
    int vboColorHandle;
    int amountOfVertices;

    public ThreeDModel(physics.Mesh m) {
        this.m = m;
        vboVertexHandle = glGenBuffers();
        vboNormalHandle = glGenBuffers();
        vboColorHandle = glGenBuffers();
        amountOfVertices = m.getFaces().size() * 9;
        FloatBuffer vertices = BufferUtils.createFloatBuffer(amountOfVertices);
        FloatBuffer normals = BufferUtils.createFloatBuffer(amountOfVertices);
        FloatBuffer colors = BufferUtils.createFloatBuffer(amountOfVertices);
        for (Face face : m.getFaces()) {
            for (int i = 0; i < 3; i++) {
                vertices.put(asFloats(m.getVertices().get(face.getVertexIndices()[i])));
                normals.put(asFloats(m.getNormals().get(face.getNormalIndices()[i])));
                colors.put(face.getMaterial().getDiffuseColor());
            }
        }
        vertices.flip();
        normals.flip();
        colors.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glNormalPointer(GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
        glColorPointer(3, GL_FLOAT, 0, 0L);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDrawArrays(GL_TRIANGLES, 0, amountOfVertices / 3);
        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    public physics.Mesh getMesh() {
        return m;
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
}
