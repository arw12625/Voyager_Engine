/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphics.BoundingBoxGraphic;
import graphics.ThreeDGraphicsManager;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import physics.AggregateEntity;
import physics.BoundingBox;
import physics.Mesh;
import physics.StaticEntity;
import resource.WavefrontModel;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class AggregateModelEntity extends AggregateEntity implements graphics.ThreeD, update.Updateable {

    graphics.ThreeDModel m;
    ArrayList<BoundingBoxGraphic> bbg;
    BoundingBoxGraphic all;

    public AggregateModelEntity(graphics.ThreeDModel m, ArrayList<StaticEntity> bodies, BoundingBox b, float mass, Matrix3f inertia) {
        super(bodies, b, mass, inertia);
        this.m = m;
        bbg = new ArrayList<BoundingBoxGraphic>();
        for(StaticEntity se : bodies) {
            BoundingBoxGraphic bggggg = new BoundingBoxGraphic(se);
            bggggg.create();
            //bbg.add(bggggg);
        }
        all = new BoundingBoxGraphic(this);
        all.create();
        //ThreeDGraphicsManager.getInstance().add(all);
    }

    public AggregateModelEntity(graphics.ThreeDModel m, AggregateEntity a) {
        this(m, a.getPhysicalBodies(), a, a.getMass(), a.getInertiaTensor());
    }

    public static AggregateModelEntity aggregateModelEntityFromPath(String prefix, String path) {
        return aggregateModelEntityFromWavefront(new WavefrontModel(prefix, path));
    }

    public static AggregateModelEntity aggregateModelEntityFromPath(String mpath) {
        return aggregateModelEntityFromWavefront(new WavefrontModel(mpath));
    }

    public static AggregateModelEntity aggregateModelEntityFromWavefront(WavefrontModel w) {
        w.create();
        while (!w.isLoaded()) {
            Thread.yield();
        }
        graphics.ThreeDModel model = new graphics.ThreeDModel(w);
        model.create();
        while (!model.isProcessed()) {
            Thread.yield();
        }
        ArrayList<StaticEntity> ents = new ArrayList<StaticEntity>();
        for (BoundingBox b : w.getCustomBounds()) {
            StaticEntity stat = new StaticEntity(b, b.getVolume());
            stat.create();
            ents.add(stat);
        }
        AggregateEntity a = AggregateEntity.aggFromBodies(ents);
        AggregateModelEntity ame = new AggregateModelEntity(model, a);
        ame.create();
        return ame;
    }

    @Override
    public void render() {
        Vector3f pos = getPosition();
        Quaternion orientation = getOrientation();
        float angle = (float) (Math.acos(orientation.getW()) * 2 * 180 / Math.PI);
        glPushMatrix();
        glTranslatef(pos.getX(), pos.getY(), pos.getZ());
        glRotatef(-angle, orientation.getX(), orientation.getY(), orientation.getZ());
        for(int i = 0; i < bbg.size(); i++) {
            bbg.get(i).setBoundingBox(getPhysicalBodies().get(i));
            bbg.get(i).render();
        }
        m.render();
        glPopMatrix();
    }

    @Override
    public boolean update(int delta) {
        return false;
    }
}
