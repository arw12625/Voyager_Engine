
var grav = new physics.Gravity();
var ter = new resource.WavefrontModel("space/water_map_fix");
ter.create();

script.ScriptUtil.waitUntilLoaded(ter);

var terDisp = new graphics.ThreeDModel(ter);
terDisp.create();
script.ScriptUtil.waitUntilLoaded(terDisp);
ThreeDGraphicsManager.add(terDisp);
var cm = new physics.CollisionMesh(ter.getObjects());
cm.create();
ThreeDPhysicsManager.setCollisionMesh(cm);

var agg = test.SimpleModelEntity.simpleModelEntityFromPath("space/solid_rocket_fix");/*AggregateModelEntity.aggregateModelEntityFromPath("landing_module_fix");*/
agg.create();
var gs = ScriptManager.loadScriptFromText("function update(delta) { /* System.out.println(1000 / delta); */ }");
gs.create();
agg.addScript(gs);
ThreeDGraphicsManager.add(agg);
agg.setPosition(new Vector3f(30, -240, 0));
ThreeDPhysicsManager.add(agg);
agg.addForceGenerator(grav);

var player = new test.FocusPlayer(agg);
player.create();
Game.setPlayer(player);
ThreeDGraphicsManager.setViewPoint(player.getViewPoint());

var keyListenerGenerator = new JavaAdapter(physics.ForceGenerator, {
    applyForce: function(physEnt) {
        
            
        if (InputManager.getKey(yolo.Keyboard.KEY_LSHIFT).isDown()) {
            var up = new Vector3f(0, 80, 0);
            physEnt.applyForce(util.Utilities.transform(up, physEnt.getOrientation()));
            physEnt.setAwake(true);
        }
        var torque = new Vector3f();
        if (InputManager.getKey(yolo.Keyboard.KEY_W).isDown()) {
            torque.setX(torque.getX() - 1);
        }
        if (InputManager.getKey(yolo.Keyboard.KEY_S).isDown()) {
            torque.setX(torque.getX() + 1);
        }
        if (InputManager.getKey(yolo.Keyboard.KEY_A).isDown()) {
            torque.setZ(torque.getZ() + 1);
        }
        if (InputManager.getKey(yolo.Keyboard.KEY_D).isDown()) {
            torque.setZ(torque.getZ() - 1);
        }
        if (InputManager.getKey(yolo.Keyboard.KEY_Q).isDown()) {
            torque.setY(torque.getY() + 1);
        }
        if (InputManager.getKey(yolo.Keyboard.KEY_E).isDown()) {
            torque.setY(torque.getY() - 1);
        }
        if(torque.lengthSquared() > 0) {
            torque.normalise();
            torque.scale(100);
            torque = util.Utilities.transform(torque, physEnt.getOrientation());
            physEnt.applyTorque(torque);
            //physEnt.applyImpulseAtPoint
        }
    }});
agg.addForceGenerator(keyListenerGenerator);
    
var rocket = test.SimpleModelEntity.simpleModelEntityFromPath("space/solid_rocket_fix");
rocket.create();
ThreeDGraphicsManager.add(rocket);
rocket.setPosition(new Vector3f(0, 8, 0));
rocket.addForceGenerator(grav);
    
var s = new graphics.SkySphere(graphics.SkySphere.SkyType.PLAIN_NIGHT);
s.create();
ThreeDGraphicsManager.addGraphic3D(s, -100);
    
testing();