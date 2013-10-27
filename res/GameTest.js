/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

importClass(Packages.test.GameTest);

function enableDebug(enable) { GameTest.enableDebug(enable); }

importPackage(Packages.resource);
importPackage(Packages.sound);
importPackage(Packages.physics);
importPackage(Packages.util);
importPackage(Packages.test);
importPackage(Packages.graphics);
importPackage(Packages.script);
importPackage(Packages.input);
importClass(org.lwjgl.input.Keyboard);
importClass(org.lwjgl.input.Mouse);
importPackage(java.lang);
importClass(org.lwjgl.util.vector.Vector3f);

FontManager.getInstance().create();
TextureManager.getInstance().create();
SoundManager.getInstance().create();
ThreeDPhysicsManager.getInstance().create();
DebugMessages.getInstance().create();
GameStateManager.getInstance().create();
        
InputManager.getInstance().put(Keyboard.KEY_UP);
InputManager.getInstance().put(Keyboard.KEY_DOWN);
InputManager.getInstance().put(Keyboard.KEY_LEFT);
InputManager.getInstance().put(Keyboard.KEY_RIGHT);
Mouse.setGrabbed(true);

var player = new VerticalPlayer();
player.create();
Game.setPlayer(player);
ThreeDGraphicsManager.getInstance().setViewPoint(player.getViewPoint());

Console.getInstance().create();
ScriptManager.getInstance().loadAndExecute("GameTest.js");

var grav = new Gravity();

var ter = new WavefrontModel("teapot_fix");
ter.create();
var terDisp = new ThreeDModel(ter);
terDisp.create();
ThreeDGraphicsManager.getInstance().add(terDisp);
var cm = new CollisionMesh(ter.getObjects());
System.out.println("EH");
System.out.println("EH");
cm.create();
ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);

System.out.println("EH");

player.getPhysicalEntity().setPosition(new Vector3f(0, 7, 0));
//player.getPhysicalEntity().addForceGenerator(grav);

var green = RigidBody.rigidBodyFromPath("box_fix");
green.create();
graphicsManager.add(green);
//green.getBounds().setOrientation(Utilities.quatFromAxisAngle(new Vector3f(0, 0, 1), -(float)Math.PI));
green.setPosition(new Vector3f(-20, 15f, 7));
physics.ThreeDPhysicsManager.getInstance().add(green);
green.addForceGenerator(grav);

var s = new SkySphere(SkySphere.SkyType.PLAIN_NIGHT);
s.create();
graphicsManager.addGraphic3D(s, -100);