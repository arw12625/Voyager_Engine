package sound;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import game.GameObject;
import static org.lwjgl.openal.AL10.*;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Sound extends GameObject implements update.Updateable {

    String name;
    int source;
    boolean playing;
    int delay;
    int counter;
    float volume;
    float mult;

    protected Sound(String name, int buffer, int delay) {
        this.source = alGenSources();
        alSourcei(source, AL_BUFFER, buffer);
        this.playing = false;
        this.delay = delay;
        this.volume = 1f;
        this.mult = 1;
    }

    protected Sound(String name, int buffer, Vector3f pos, int delay) {
        this(name, buffer, delay);
        setPosition(pos);
    }
    
    @Override
    public void update(int delta) {
        if (playing) {
            if ((counter -= delta) < 0) {
                playing = false;
            }
        }
    }
    
    public void setPosition(Vector3f pos) {
        alSource3f(source, AL_POSITION, pos.getX() / 100, pos.getY() / 100, pos.getZ() / 100);
    }

    public int getSource() {
        return source;
    }

    public void play(boolean force) {
        if (!playing || force) {
            alSourcePlay(getSource());
            playing = true;
            counter = delay;
        }
    }

    public void play() {
        play(false);
    }

    public void stop() {
        alSourceStop(getSource());
        playing = false;
    }

    public void loop() {
        loop(true);
    }
    
    public void loop(boolean loop) {
        alSourcei(source, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public void setVolume(float volume) {
        this.volume = volume;
        alSourcef(source, AL_GAIN, volume * mult);
    }

    public void mute(boolean muted) {
        if (muted) {
            alSourcef(source, AL_GAIN, 0);
        } else {
            setVolume(volume);
        }
    }

    public void setMult(float mult) {
        this.mult = mult;
        setVolume(this.volume);
    }

    
}
