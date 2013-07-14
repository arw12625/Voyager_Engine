/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import util.Utilities;
import sound.Sound;
import java.io.BufferedInputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import static org.lwjgl.openal.AL10.*;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;
import sound.SoundResource;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class SoundManager extends Manager implements update.Updateable{

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
        DebugMessages.getInstance().write("SoundManager created");
    }

    public static SoundManager getInstance() {
        if(instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        AL.destroy();
        DebugMessages.getInstance().write("SoundManager destroyed");
    }

    @Override
    public void update(int delta) {
        for(Sound s : sounds.values()) {
            s.update(delta);
        }
        DebugMessages.getInstance().write("SoundManager updated");
    }
    
    public void setListenerPVector3fosition(Vector3f r) {
        alListener3f(AL_POSITION, r.getX() * xScale, r.getY() * yScale, r.getZ() * zScale);
    }

    public Sound createSound(String name, int buffer, int delay) {
        Sound s = new Sound(name, buffer, delay);
        sounds.put(s.getFullName(), s);
        DebugMessages.getInstance().write("Sound created: " + s.getFullName());
        return s;
    }
    
    public Sound createSound(String name, SoundResource r) {
        return createSound(name, r.getBuffer(), r.getLength());
    }

    public void addSoundResource(SoundResource s) {
        resources.put(s.getFullName(), s);
    }

    public SoundResource loadSoundResource(String path) {
        SoundResource resource = new SoundResource(path);
        Game.resourceManager.loadResource(resource);
        addSoundResource(resource);
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

    public void setVolume(float volume) {
        for (Sound s : sounds.values()) {
            s.setMult(volume);
        }
    }

    @Override
    public String getFullName() {
        return "SoundManager";
    }

    @Override
    public boolean add(GameObject obj) {
        if(obj instanceof Sound) {
            sounds.put(obj.getFullName(), (Sound)obj);
            return true;
        }
        if(obj instanceof SoundResource) {
            resources.put(obj.getFullName(), (SoundResource)obj);
            return true;
        }
        return false;
    }

    @Override
    public void remove(GameObject obj) {
        if(sounds.containsKey(obj.getFullName())) {
            sounds.remove(obj.getFullName());
        }
        if(resources.containsKey(obj.getFullName())) {
            resources.remove(obj.getFullName());
        }
    }

}
