/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphics.ThreeD;
import graphics.ThreeDModel;
import java.util.ArrayList;
import physics.PhysicalEntity;
import physics.Plane;

/**
 *
 * @author Andy
 */
public abstract class AbstractEntity extends PhysicalEntity implements ThreeD {

    ThreeDModel m;
    
    public AbstractEntity(ThreeDModel m) {
        super(m.getMesh().getBounds());
        this.m = m;
    }

    @Override
    public void update(int delta) {
    }
    
    @Override
    public void render() {
        m.render();
    }

    
}
