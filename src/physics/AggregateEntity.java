/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class AggregateEntity extends DynamicEntity {

    ArrayList<StaticEntity> bodies;
    Vector3f offset;

    public AggregateEntity(BoundingBox b) {
        this(b, defaultMass);
    }

    public AggregateEntity(BoundingBox b, float mass) {
        this(b, mass, getRectangularPrismInertiaTensor(b, mass));
    }

    public AggregateEntity(final BoundingBox b, final float mass, final Matrix3f inertiaTensor) {
        this(new ArrayList<StaticEntity>(), b, mass, inertiaTensor);
    }

    public AggregateEntity(ArrayList<StaticEntity> bodies, BoundingBox b, float mass, Matrix3f inertiaTensor) {
        this(bodies, b, mass, inertiaTensor, Utilities.zeroVec, Utilities.zeroVec);
    }
    
    public AggregateEntity(ArrayList<StaticEntity> bodies, BoundingBox b, float mass, Matrix3f inertiaTensor, Vector3f centerOfMass, Vector3f offset) {
        super(b, mass, inertiaTensor, centerOfMass);
        this.offset = offset;
        this.bodies = bodies;
    }
    
    @Override
    public void integrate(float time) {
        super.integrate(time);
    }

    @Override
    public ArrayList<Vector3f> getContactVertices() {
        ArrayList<Vector3f> contacts = new ArrayList<Vector3f>();
        if (isCollidable()) {
            for (StaticEntity s : bodies) {
                contacts.addAll(s.getContactVertices());
            }
            for (int i = 0; i < contacts.size(); i++) {
                Vector3f v = Utilities.transform(contacts.get(i), getOrientation());
                Vector3f.add(v, getPosition(), v);
                contacts.set(i, v);
            }
        }
        return contacts;
    }

    public static AggregateEntity aggFromBodies(ArrayList<StaticEntity> bodies) {
        BoundingBox b = BoundingBox.boundsFromBounds(bodies, null);
        Vector3f offset = b.getPosition();
        offset.negate();
        float mass = 0;
        Matrix3f inertia = new Matrix3f();
        Vector3f centerOfMass = new Vector3f();
        for (StaticEntity p : bodies) {
            Vector3f.add(centerOfMass, (Vector3f)p.getGlobalCenterOfMass().scale(p.getMass()), centerOfMass);
            mass += p.getMass();
            /*Matrix3f skew = Utilities.skewMatrix(p.getPosition());
            Matrix3f.mul(skew, skew, skew);
            Utilities.scale(skew, p.getMass());
            Matrix3f rot = Utilities.matFromQuat(p.getBounds().getOrientation());
            Matrix3f transformedInertia = Matrix3f.mul(Matrix3f.mul(rot, p.getInertiaTensor(), null), rot.transpose(null), null);
            Matrix3f.add(Matrix3f.sub(transformedInertia, skew, null), inertia, inertia);*/
        }
        centerOfMass.scale(1f / mass);
        for(StaticEntity p : bodies) {
            Matrix3f parallel = new Matrix3f();
            Matrix3f.setIdentity(parallel);
            parallel = Utilities.scale(parallel, p.getGlobalCenterOfMass().lengthSquared());
            Matrix3f.sub(parallel, Utilities.outerProduct(p.getGlobalCenterOfMass(), p.getGlobalCenterOfMass()),parallel);
            Utilities.scale(parallel, mass);
            Matrix3f.add(inertia, parallel, inertia);
            Matrix3f.add(inertia, p.getInertiaTensor(), inertia);
        }
        AggregateEntity ae = new AggregateEntity(bodies, b, mass, inertia, centerOfMass, offset);
        ae.create();
        return ae;
    }

    public ArrayList<StaticEntity> getLocalPhysicalBodies() {
        return bodies;
    }

    public ArrayList<StaticEntity> getTransformedPhysicalBodies() {
        ArrayList<StaticEntity> transformed = new ArrayList<StaticEntity>();
        for (StaticEntity s : bodies) {
            StaticEntity post = new StaticEntity(s, s.getMass(), s.getInertiaTensor());
            post.create();
            Vector3f rotate = Utilities.transform(post.getPosition(), orientation);
            post.setOrientation(Utilities.transform(orientation, post.getOrientation()));
            post.setPosition(Vector3f.add(position, rotate, null));
            transformed.add(post);
        }
        return transformed;
    }
}
