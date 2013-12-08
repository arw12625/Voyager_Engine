/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import game.GameObject;
import game.Manager;
import util.Utilities;
import java.util.HashMap;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import static org.lwjgl.openal.AL10.*;
import org.lwjgl.util.vector.Vector3f;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class SoundManager extends Manager implements update.Updateable {

    HashMap<String, SoundResource> resources;
    HashMap<String, Sound> sounds;
    private boolean muted;
    float xScale = 0.01f, yScale = 0.01f, zScale = 0.01f;
    static SoundManager instance;

    @Override
    public void create() {
        super.create();
        try {
            AL.create();
            alListener(AL_ORIENTATION, Utilities.asFloatBuffer(new float[]{0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}));
        } catch (LWJGLException e) {
            e.printStackTrace();
            AL.destroy();
        }
        sounds = new HashMap<String, Sound>();
        resources = new HashMap<String, SoundResource>();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    @Override
    public void destroy() {
        super.destroy();
        AL.destroy();
    }

    @Override
    public boolean update(int delta) {
        for (Sound s : sounds.values()) {
            s.update(delta);
        }
        DebugMessages.getInstance().write("SoundManager updated");

        return false;
    }

    public void setListenerPVector3fosition(Vector3f r) {
        alListener3f(AL_POSITION, r.getX() * xScale, r.getY() * yScale, r.getZ() * zScale);
    }

    public Sound createSound(String name, int buffer, int delay) {
        Sound s = new Sound(name, buffer, delay);
        sounds.put(s.getFullName(), s);
        return s;
    }

    public Sound createSound(String name, SoundResource r) {
        return createSound(name, r.getBuffer(), r.getLength());
    }

    public SoundResource loadSoundResource(String path) {
        SoundResource resource = new SoundResource(path);
        resources.put(resource.getFullName(), resource);
        return resource;
    }

    public void mute(boolean muted) {
        this.muted = muted;
        for (Sound s : sounds.values()) {
            s.mute(muted);
        }
        DebugMessages.getInstance().write("Muted: " + muted);
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMasterVolume(float volume) {
        for (Sound s : sounds.values()) {
            s.setMult(volume);
        }
    }

    @Override
    public void remove(GameObject obj) {
        if (sounds.containsKey(obj.getFullName())) {
            sounds.remove(obj.getFullName());
        }
        if (resources.containsKey(obj.getFullName())) {
            resources.remove(obj.getFullName());
        }
    }
}
