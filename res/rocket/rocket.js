/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var engineRunning = false;
var parachuteDeployed = false;

var engineSoundRes = SoundManager.getInstance().loadSoundResource("vroom.wav");
engineSoundRes.create();
ScriptUtil.waitUntilLoaded(engineSoundRes);
var engineSound = SoundManager.getInstance().createSound("vroom", engineSoundRes);
engineSound.create();
engineSound.loop(true);

var boomSoundRes = SoundManager.getInstance().loadSoundResource("boom.wav");
boomSoundRes.create();
ScriptUtil.waitUntilLoaded(boomSoundRes);
var boomSound = SoundManager.getInstance().createSound("boom", boomSoundRes);
boomSound.create();

var exhaustWave = new WavefrontModel("exhaust");
exhaustWave.create();
ScriptUtil.waitUntilLoaded(exhaustWave);
var exhaust = new ThreeDModel(exhaustWave);
exhaust.create();
ScriptUtil.waitUntilLoaded(exhaust);
var exhaustPos = new Vector3f(0, -2.4, 0);

var parachuteWave = new WavefrontModel("parachute");
parachuteWave.create();
ScriptUtil.waitUntilLoaded(parachuteWave);
var parachute = new ThreeDModel(parachuteWave);
parachute.create();
ScriptUtil.waitUntilLoaded(parachute);
var parachutePos = new Vector3f(0, 4.45, 0);
var forcePos = new Vector3f(0, 1, 0);

var keyListenerGenerator = new JavaAdapter(ForceGenerator, {
    applyForce: function(physEnt) {
        engineRunning = InputManager.getInstance().get(Keyboard.KEY_LSHIFT).isDown();
        parachuteDeployed = InputManager.getInstance().get(Keyboard.KEY_SPACE).isDown();
        engineSound.setPosition(currentObject.getPosition());
        if (engineRunning) {
			parachuteDeployed = false;
           engineSound.play();
            var up = new Vector3f(0, 180, 0);
            physEnt.applyForce(Utilities.transform(up, physEnt.getOrientation()));
            physEnt.setAwake(true);
        } else {
            engineSound.stop();
        }
        
        var torque = new Vector3f();
        if (InputManager.getInstance().get(Keyboard.KEY_W).isDown()) {
            torque.setX(torque.getX() - 1);
        }
        if (InputManager.getInstance().get(Keyboard.KEY_S).isDown()) {
            torque.setX(torque.getX() + 1);
        }
        if (InputManager.getInstance().get(Keyboard.KEY_A).isDown()) {
            torque.setZ(torque.getZ() + 1);
        }
        if (InputManager.getInstance().get(Keyboard.KEY_D).isDown()) {
            torque.setZ(torque.getZ() - 1);
        }
        if (InputManager.getInstance().get(Keyboard.KEY_Q).isDown()) {
            torque.setY(torque.getY() + 1);
        }
        if (InputManager.getInstance().get(Keyboard.KEY_E).isDown()) {
            torque.setY(torque.getY() - 1);
        }
        if(torque.lengthSquared() > 0) {
            torque.normalise();
            torque.scale(25);
            physEnt.applyTorque(Utilities.transform(torque, physEnt.getOrientation()));
        }
		
		if(parachuteDeployed) {
			var dir = physEnt.getVelocity();
			var mag = dir.lengthSquared() * 4;
			dir = dir.normalise(null);
			dir.negate();
			dir.scale(mag);
			var appPos = Vector3f.add(currentObject.getPosition(), Utilities.transform(forcePos, currentObject.getOrientation()), null);
			physEnt.applyForceAtPoint(dir,appPos);
		}
    }});
currentObject.addForceGenerator(keyListenerGenerator);

function update(delta) {
    
    if(currentObject.getPosition().getY() < -169) {
		engineRunning = false;
		parachuteDeployed = false;
		engineSound.stop();
		boomSound.play();
        print("=====================\n" + 
              "=      YOU LOSE     =\n" +
              "=====================");
        currentObject.destroy();
    }
}

function render() {
    if(parachuteDeployed) {
		var pos = Vector3f.add(currentObject.getPosition(), Utilities.transform(parachutePos, currentObject.getOrientation()), null);
        var orientation = currentObject.getOrientation();
        var angle = (Math.acos(orientation.getW()) * 2 * 180 / Math.PI);
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.getX(), pos.getY(), pos.getZ());
        GL11.glRotatef(-angle, orientation.getX(), orientation.getY(), orientation.getZ());
        parachute.render();
        GL11.glPopMatrix();
	}
    if(engineRunning) {
        var pos = Vector3f.add(currentObject.getPosition(), Utilities.transform(exhaustPos, currentObject.getOrientation()), null);
        var orientation = currentObject.getOrientation();
        var angle = (Math.acos(orientation.getW()) * 2 * 180 / Math.PI);
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.getX(), pos.getY(), pos.getZ());
        GL11.glRotatef(-angle, orientation.getX(), orientation.getY(), orientation.getZ());
        GL11.glRotatef(180, 1, 0, 0);
        exhaust.render();
        GL11.glPopMatrix();
    }
}
function integrate(time) { currentObject.applyForce(new Vector3f(0, -10, 0));}