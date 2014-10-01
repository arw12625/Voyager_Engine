/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.*;
import graphics.BoundingBoxGraphic;
import graphics.ThreeDGraphicsManager;
import graphics.VectorGraphic;
import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;
import util.DebugMessages;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class ThreeDPhysicsManager extends StandardManager implements update.Updateable {

    ArrayList<StaticEntity> se;
    ArrayList<DynamicEntity> de;
    CollisionMesh collisionMesh;
    float restitution = .3f;
    static ThreeDPhysicsManager instance;
    private float coefficientOfFriction = .3f;

    @Override
    public void create() {
        super.create();
        se = new ArrayList<StaticEntity>();
        de = new ArrayList<DynamicEntity>();
        
    }

    public static ThreeDPhysicsManager getInstance() {
        if (instance == null) {
            instance = new ThreeDPhysicsManager();
        }
        return instance;
    }

    @Override
    public boolean update(int delta) {

        DebugMessages.getInstance().write("Physics starting");

        //Motion update

        //The number of iterations physics is applied in one call
        float division = 1;
        //The number of collisions resolved in one iteration
        float collision = 1;
        //time in seconds between each iteration
        float time = delta / 1000f / division;

        //Main physics section
        for (int j = 0; j < division; j++) {

            //Update the dynamics and kinematics of the entities
            for (DynamicEntity e : de) {

                e.updateForces();
                if (e.isAwake()) {
                    e.runScripts("integrate", new Object[]{time});
                    e.integrate(time);
                }
                e.resetBuffers();

            }

            //Copy the list of entities for collision
            ArrayList<StaticEntity> pcopy = new ArrayList<StaticEntity>(se);
            //Loop through all entities for collision
            for (final StaticEntity e : se) {

                //If the entity is dynamic, create a typecast copy
                boolean dynamic = e instanceof DynamicEntity;
                DynamicEntity cast = null;
                if (dynamic) {
                    cast = (DynamicEntity) e;
                }

                //Check if there is a collision mesh
                if (collisionMesh != null && !collisionMesh.isEmpty()) {

                    //Query all collisions with the current entity
                    ArrayList<Plane> cols = collisionMesh.getPlanes(e);
                    
                    //System.out.println("COLLISIONS " + cols);
                    
                    //Continue if collisions are detected
                    if (!cols.isEmpty()) {
                        
                        ArrayList<Vector3f> vertices;
                        //Loop through each collision
                        for (int i = 0; i < collision; i++) {
                            float highestVelocity = Float.MAX_VALUE;
                            //The plane of the collision mesh that is colliding
                            Plane collisionPlane = null;
                            //The specific vertex of the entity that is colliding
                            Vector3f collisionVector = null;
                            //The velocity difference of the entity and plane
                            Vector3f collisionVelocity = null;
                            //Retrieve any sub bodies
                            ArrayList<StaticEntity> bodies = e instanceof AggregateEntity
                                    ? ((AggregateEntity) e).getTransformedPhysicalBodies()
                                    : new ArrayList<StaticEntity>() {{ add(e);}};
                            //Loop through each body to calculate collision info
                            for (StaticEntity s : bodies) {
                                //all vertices in the subbody that are collidible
                                vertices = s.getContactVertices();
                                for (Vector3f v : vertices) {
                                    //The moment arm of the contact from the center of mass
                                    Vector3f relativeContact = Vector3f.sub(v, e.getGlobalCenterOfMass(), null);
                                    //Iterate through each plane that collided witht the bounds
                                    for (Plane p : cols) {
                                        //Get the perpindicular distance from the plane to the contact
                                        float distance = p.getDistance(v);
                                        //Check to see if the contact is behind the plane
                                        if (distance < 0) {
                                            //provide which plane and vertex is colliding to the entity
                                            e.collide(p, v);
                                            //if the entity is dynamic, gather physics information
                                            if (dynamic) {
                                                //The velocity of the contact due to rotational velocity
                                                Vector3f angularComponent = Vector3f.cross(cast.getAngularVelocity(), relativeContact, null);
                                                //The velocity of the contact
                                                Vector3f pointVelocity = Vector3f.add(cast.getVelocity(), angularComponent, null);
                                                //The velocity at which the contact moves away from the plane
                                                float seperatingVelocity = Vector3f.dot(pointVelocity, p.getNormal());
                                                //Check to see if the contact is moving into the plane
                                                //and if it is the fastest collision
                                                if (seperatingVelocity < highestVelocity) {
                                                    //if so, store the information
                                                    highestVelocity = seperatingVelocity;
                                                    collisionPlane = p;
                                                    collisionVector = v;
                                                    collisionVelocity = pointVelocity;
                                                    
                                                }
                                            }
                                        }
                                    }           //I call it cascading curly brackets
                                }
                            }
                            
                            //If the entity was dynamic, resolve the collision from stored information
                            if (dynamic) {
                                //Check to see if the collision won't resolve itself
                                if (highestVelocity < 0) {
                                    
                                    Vector3f normal = new Vector3f(collisionPlane.getNormal());
                                    
                                    //The velocity accumulated due to acceleration this frame
                                    //float accCausedV = Vector3f.dot((Vector3f) (new Vector3f(cast.getAcceleration()).scale(time)), normal);
                                    
                                    //Calculate the speed the entity will leave the collision
                                    float newVelocity = -highestVelocity;
                                    newVelocity *= restitution;
                                    
                                    //Determine the necassary change in speed
                                    float deltaV = newVelocity - highestVelocity;
                                    //The moment arm from the c.o.m.
                                    Vector3f relativeContact = Vector3f.sub(collisionVector, cast.getGlobalCenterOfMass(), null);
                                    
                                    //Warning: complicated physics below, avert eyes
                                    //Calculate the change in velocity for a given impulse along the collision normal
                                    Vector3f angularDeltaPerImpulse = Vector3f.cross(relativeContact, normal, null);
                                    //Matrix3f.transform(e.invInertiaTensorWorld(), angularDeltaPerImpulse, angularDeltaPerImpulse);
                                    angularDeltaPerImpulse.scale(.1f);
                                    //System.out.println(angularDeltaPerImpulse);
                                    Vector3f.cross(angularDeltaPerImpulse, relativeContact, angularDeltaPerImpulse);
                                    float deltaVPerImpulse = cast.getInvMass() + Vector3f.dot(angularDeltaPerImpulse, normal);
                                    
                                    //Calculate the impulse vector 
                                    float impulse = deltaV / deltaVPerImpulse;
                                    Vector3f impulseVector = new Vector3f(normal);
                                    impulseVector.scale(impulse);
                                    
                                    //Determine frictional effects
                                    //Determine the horrizontal velocity relative to the plane
                                    Vector3f slide = Vector3f.sub(collisionVelocity, (Vector3f) (new Vector3f(normal).scale(highestVelocity)), null);
                                    Vector3f friction = new Vector3f();
                                    //If there is significant sliding, create friction
                                    if (slide.lengthSquared() > .1f) {
                                        slide.normalise();
                                        friction = (Vector3f) slide.scale(-coefficientOfFriction * impulse);
                                    }

                                    //The final calculated impulse
                                    Vector3f finalImpulse = Vector3f.add(impulseVector, friction, null);
                                    //apply the impulse at the contact
                                    cast.applyImpulseAtPointGlobal(finalImpulse, collisionVector);

                                }
                            }
                        }
                        /*for (int i = 0; i < collision; i++) {
                            vertices = e.getContactVertices();
                            float highestPenetration = Float.MAX_VALUE;
                            Plane collisionPlane = null;
                            Vector3f collisionVector = null;
                            for (Vector3f v : vertices) {
                                for (Plane p : cols) {
                                    float distance = p.getDistance(v);
                                    if (distance < 0 && distance < highestPenetration) {
                                        e.collide(p, v);
                                        highestPenetration = distance;
                                        collisionVector = v;
                                        collisionPlane = p;
                                    }
                                }
                            }
                            if (dynamic && highestPenetration < 0) {
                                //e.setPosition(Utilities.addScaledVector(e.getPosition(), collisionPlane.getNormal(), -.005f * highestPenetration));
                            }
                        }*/
                    }
                }
                pcopy.remove(e);

                for (StaticEntity other : pcopy) {
                    if (e.getBounds().intersects(other.getBounds())) {
                        e.collide(other);
                        other.collide(e);
                    }
                }
            }
        }

        DebugMessages.getInstance().write("Physics finished");

        return false;
    }

    @Override
    public boolean add(GameObject obj) {
        if (obj instanceof StaticEntity) {
            se.add((StaticEntity) obj);
            if (obj instanceof DynamicEntity) {
                de.add((DynamicEntity) obj);
            }
            return true;
        }
        return false;
    }

    @Override
    public void remove(GameObject obj) {
        if (se.contains(obj)) {
            se.remove(obj);
            if (de.contains(obj)) {
                de.remove(obj);
            }
        }
    }

    public void setCollisionMesh(CollisionMesh cm) {
        this.collisionMesh = cm;
    }

    public CollisionMesh getCollisionMesh() {
        return collisionMesh;
    }
}
