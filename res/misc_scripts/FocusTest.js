/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

importClass(Packages.test.GameTest);

importPackage(Packages.game);
importPackage(Packages.resource);
importPackage(Packages.sound);
importPackage(Packages.physics);
importPackage(Packages.util);
importPackage(Packages.test);
importPackage(Packages.graphics);
importPackage(Packages.script);
importPackage(Packages.input);
importClass(org.lwjgl.input.Mouse);
importPackage(java.lang);
importClass(org.lwjgl.util.vector.Vector3f);

FontManager.getInstance().create();
TextureManager.getInstance().create();
SoundManager.getInstance().create();
ThreeDPhysicsManager.getInstance().create();
DebugMessages.getInstance().create();
GameStateManager.getInstance().create();
Console.getInstance().create();

Mouse.setGrabbed(true);

var grav = new Gravity();

var ter = new WavefrontModel("launch_pad_fix");
ter.create();

ScriptUtil.waitUntilLoaded(ter);
var terDisp = new ThreeDModel(ter);
terDisp.create();
ScriptUtil.waitUntilLoaded(terDisp);
ThreeDGraphicsManager.getInstance().add(terDisp);
var cm = new CollisionMesh(ter.getObjects());
cm.create();
ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);
    
var rocket = RigidBody.rigidBodyFromPath("solid_rocket_fix");
rocket.create();
ThreeDGraphicsManager.getInstance().add(rocket);
rocket.setPosition(new Vector3f(0, 8, 0));
ThreeDPhysicsManager.getInstance().add(rocket);
rocket.addForceGenerator(grav);

var player = new FocusPlayer(rocket);
player.create();
Game.setPlayer(player);
ThreeDGraphicsManager.getInstance().setViewPoint(player.getViewPoint());

var s = new SkySphere(SkySphere.SkyType.PLAIN_DAY);
s.create();
ThreeDGraphicsManager.getInstance().addGraphic3D(s, -100);
    
Game.setInitializing(false);
