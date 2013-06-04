/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import resource.TextureResource;

/**
 *
 * @author Andy
 */
public class Material {

    private float[] ambientColor; // 0-1
    private float[] diffuseColor; // 0-1
    private float[] specularColor; // 0-1
    private float specularCoefficient; // 0-1000
    private TextureResource texture;
    
    public static Material defaultMaterial = new Material(new float[] {.3f, .3f, .3f});

    public Material() {
        
    }
    
    public Material(float[] ambientColor) {
        this(ambientColor, null, null, 0);
    }

    public Material(float[] ambientColor, float[] diffuseColor, float[] specularColor, float specularCoefficient) {
        this(ambientColor, diffuseColor, specularColor, specularCoefficient, null);
    }

    public Material(float[] ambientColor, float[] diffuseColor, float[] specularColor, float specularCoefficient, TextureResource texture) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.specularCoefficient = specularCoefficient;
        this.texture = texture;
    }

    public boolean isTextured() {
        return texture != null;
    }

    public float[] getAmbientColor() {
        return ambientColor;
    }

    public float[] getDiffuseColor() {
        return diffuseColor;
    }

    public float[] getSpecularColor() {
        return specularColor;
    }

    public float getSpecularCoefficient() {
        return specularCoefficient;
    }

    public TextureResource getTexture() {
        return texture;
    }
    
    public void setAmbientColor(float[] ambientColor) {
        this.ambientColor = ambientColor;
    }
    
    public void setDiffuseColor(float[] diffuseColor) {
        this.diffuseColor = diffuseColor;
    }
    
    public void setSpecularColor(float[] specularColor) {
        this.specularColor = specularColor;
    }

    public void setSpecularCoefficient(float specularCoefficient) {
        this.specularCoefficient = specularCoefficient;
    }

    public void setTexture(TextureResource texture) {
        this.texture = texture;
    }
    
    @Override
        public String toString() {
            return "Material{" +
                    ", ambientColour=" + ambientColor +
                    ", diffuseColour=" + diffuseColor +
                    ", specularColour=" + specularColor +
                    "specularCoefficient=" + specularCoefficient +
                    '}';
        }
}