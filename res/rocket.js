/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var engineRunning = false;

var engineSoundRes = SoundManager.getInstance().loadSoundResource("vroom.wav");
engineSoundRes.create();
ScriptUtil.waitUntilLoaded(engineSoundRes);
var engineSound = SoundManager.getInstance().createSound("vroom", engineSoundRes);
engineSound.create();
engineSound.loop(true);

var exhaustWave = new WavefrontModel("exhaust");
exhaustWave.create();
ScriptUtil.waitUntilLoaded(exhaustWave);
var exhaust = new ThreeDModel(exhaustWave);
exhaust.create();
ScriptUtil.waitUntilLoaded(exhaust);
var exhaustPos = new Vector3f(0, -2.4, 0);


var keyListenerGenerator = new JavaAdapter(ForceGenerator, {
    applyForce: function(physEnt) {
        engineRunning = InputManager.getInstance().get(Keyboard.KEY_LSHIFT).isDown();
        engineSound.setPosition(currentObject.getPosition());
        if (engineRunning) {
            engineSound.play();
            var up = new Vector3f(0, 80, 0);
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
    }});4
currentObject.addForceGenerator(keyListenerGenerator);

function update(delta) {
    
    if(currentObject.getPosition().getY() < -169) {
        print("=====================\n" + 
              "=      YOU LOSE     =\n" +
              "=====================");
        currentObject.destroy();
    }
}

function render() {
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