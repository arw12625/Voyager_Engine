/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

/**
 *
 * @author Andy
 */
public class Face {

    private int[] vertexIndices;
    private int[] normalIndices;
    private int[] texCoordIndices;

    public int[] getVertexIndices() {
        return vertexIndices;
    }

    public int[] getTextureCoordinateIndices() {
        return texCoordIndices;
    }

    public int[] getNormalIndices() {
        return normalIndices;
    }

    public Face(int[] vertexIndices, int[] normalIndices) {
        this(vertexIndices, normalIndices, null);
    }

    public Face(int[] vertexIndices, int[] normalIndices, int[] texCoordIndices) {
        this.vertexIndices = vertexIndices;
        this.normalIndices = normalIndices;
        this.texCoordIndices = texCoordIndices;
    }
}
