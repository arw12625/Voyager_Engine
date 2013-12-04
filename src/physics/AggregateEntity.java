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
        super(b, mass, inertiaTensor);
        this.bodies = bodies;
        reset();
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
        BoundingBox b = BoundingBox.boundsFromBounds(bodies);
        b.setPosition(new Vector3f());
        float mass = 0;
        Matrix3f inertia = new Matrix3f();
        for (StaticEntity p : bodies) {
            mass += p.getMass();
            Matrix3f skew = Utilities.skewMatrix(p.getPosition());
            Matrix3f.mul(skew, skew, skew);
            Utilities.scale(skew, p.getMass());
            Matrix3f rot = Utilities.matFromQuat(p.getBounds().getOrientation());
            Matrix3f transformedInertia = Matrix3f.mul(Matrix3f.mul(rot, p.getInertiaTensor(), null), rot.transpose(null), null);
            Matrix3f.add(Matrix3f.sub(transformedInertia, skew, null), inertia, inertia);
        }
        AggregateEntity ae = new AggregateEntity(bodies, b, mass, inertia);
        ae.create();
        return ae;
    }

    public ArrayList<StaticEntity> getPhysicalBodies() {
        return bodies;
    }
}
