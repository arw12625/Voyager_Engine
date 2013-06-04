/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import game.Resource;
import java.io.BufferedInputStream;
import org.lwjgl.util.WaveData;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.openal.AL10.*;

/**
 *
 * @author Andy
 */
public class SoundResource implements Resource {

    String name;
    String path;
    int bufferNum;
    int length;

    public SoundResource(String path) {
        this(path, path);
    }

    public SoundResource(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean load() {
        WaveData data = null;
        data = WaveData.create(new BufferedInputStream(ResourceLoader.getResourceAsStream(path)));
        bufferNum = alGenBuffers();
        alBufferData(bufferNum, data.format, data.data, data.samplerate);
        length = (int) ((float) data.data.capacity()
                / (data.format == AL_FORMAT_STEREO16 ? 4 : 2)
                / data.samplerate * 1000);
        data.dispose();
        return true;
    }

    @Override
    public void release() {
        alDeleteBuffers(bufferNum);
    }
    
    public int getBuffer() {
        return bufferNum;
    }
    
    public int getLength() {
        return length;
    }
}