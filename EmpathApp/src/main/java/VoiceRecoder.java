package main.java;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VoiceRecoder {

    static final long RECORD_TIME = 5000;  // 5 seconds
    File wavFile = new File("assetforceData.wav");

    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine line;

    AudioFormat getAudioFormat() {
        float sampleRate =  11025;//required by empath API
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    void start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("このラインはサポートされていません。Audioのフォーマットを確認してください");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            System.out.println("録音システム起動中.....");
            System.out.println("これから" + RECORD_TIME/1000 + "秒間録音します。" );

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("録音開始！");
            Timer timer = new Timer();
            timer.schedule(new Recoding(), 0, 1000);
            AudioSystem.write(ais, fileType, wavFile);
            timer.cancel();

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    void finish() {
        line.stop();
        line.close();
        System.out.println("録音完了しました。音声データを　assetforce-data.wav　に保存しました。\n");
    }

    class Recoding extends TimerTask {
        public void run() {
            System.out.println("１秒経過");
        }
    }

    public static void main() {
        final VoiceRecoder recorder = new VoiceRecoder();

        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });

        stopper.start();
        recorder.start();
    }
}