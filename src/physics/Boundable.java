/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

/**
 *
 * @author Andy
 */
public interface Boundable extends Positionable {
    
    public BoundingBox getBounds();
    
}
