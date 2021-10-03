package TuiXiangZi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.File;

/**
 * @author GaoMing
 * @date 2021/10/3 - 17:13
 */
public class Sound {
    String path=new String("musics\\"); // 以变量存储地址，方便日后修改
    String  file=new String("nor.mid");
    Sequence seq;
    Sequencer midi; // 负责播放MIDI序列的对象
    boolean sign;
    void loadSound()
    {
        try {
            seq= MidiSystem.getSequence(new File(path+file)); // 从musics文件夹中获得MIDI序列
            midi=MidiSystem.getSequencer();
            midi.open();
            midi.setSequence(seq);
            midi.start();
            midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);  // 音乐循环播放
        }
        catch (Exception ex) {ex.printStackTrace();}
        sign=true;
    }
    void mystop(){midi.stop();midi.close();sign=false;}
    boolean isplay(){return sign;}
    void setMusic(String e){file=e;}
}
