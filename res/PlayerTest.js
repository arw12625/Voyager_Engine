/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
importPackage(Packages.physics);
importPackage(Packages.graphics);
importClass(Packages.game.Game);
importClass(org.lwjgl.util.vector.Vector3f);
importClass(org.lwjgl.util.vector.Quaternion);

var grav = new Gravity();
var ter = new Mesh("loop-smooth_fix");
ter.create();
var terDisp = new ThreeDModel(ter);
terDisp.create();
ThreeDGraphicsManager.getInstance().add(terDisp);
var cm = new CollisionMesh(ter);
cm.create();
ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);

Game.player.getPhysicalEntity().setPosition(new Vector3f(0, 7, 0));
//Game.player.getPhysicalEntity().addForceGenerator(grav);
