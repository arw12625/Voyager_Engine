/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphics.ThreeDGraphic;
import graphics.ThreeDModel;
import java.util.ArrayList;
import physics.PhysicalEntity;
import physics.Plane;

/**
 *
 * @author Andy
 */
public abstract class AbstractEntity extends PhysicalEntity implements ThreeDGraphic {

    ThreeDModel m;
    
    public AbstractEntity(ThreeDModel m) {
        super(m.getMesh().getBounds());
        this.m = m;
    }

    @Override
    public void update(int delta) {
        m.update(delta);
    }
    
    @Override
    public void render() {
        m.render();
    }

    
}
