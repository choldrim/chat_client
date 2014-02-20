package chat.client.view;

import java.applet.Applet;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import chat.client.common.CommonData;

/**
 * 说明：声音类（单例）
 * 功能：播放声音
 * 
 * @author choldrim
 *         编写时间：2013-06-08
 */
public class Sounds extends Applet
{
    private static final long   serialVersionUID = -5810929408459036098L;

    private static final Sounds sounds           = new Sounds();         // 静态实例

    private static AudioStream  msgAudioStream;                          // 新消息声音

    private Sounds()
    {
        // 初始化 消息声音
        initMsgAudio();
    }

    /**
     * 返回本类实例
     * 
     * @return
     */
    public static Sounds getSounds()
    {
        return Sounds.sounds;
    }

    /**
     * 初始化消息声音
     */
    private void initMsgAudio()
    {
        try
        {
            String msgAudioPath = URLDecoder.decode(CommonData.class.getResource("/").toString()
                    + "chat/client/view/sound/Message.wav", "utf-8");

            // 要除去getResource 返回的路径中的 "file:/" 才能进行读取
            msgAudioPath = msgAudioPath.replace("file:/", "");
            msgAudioStream = new AudioStream(new FileInputStream(msgAudioPath));

        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 播放消息声音
     */
    public void startMessageAudio()
    {
        startAudio(msgAudioStream); // 播放声音
    }

    /**
     * 停止消息声音
     */
    public void stopMessageAudio()
    {
        stopAudio(msgAudioStream); // 停止声音
    }

    /**
     * 播放声音
     * 
     * @param audioStream_
     */
    private void startAudio(AudioStream audioStream_)
    {
        AudioPlayer.player.start(audioStream_);
    }

    /**
     * 停止声音
     * 
     * @param audioStream_
     */
    private void stopAudio(AudioStream audioStream_)
    {
        AudioPlayer.player.stop(audioStream_);
    }

}
