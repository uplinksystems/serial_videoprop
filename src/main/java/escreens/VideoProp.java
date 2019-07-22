package escreens;

import com.pi4j.io.gpio.*;
import com.sun.javafx.logging.JFRInputEvent;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

public class VideoProp {

    boolean triggered = false;

    private VideoProp() throws Exception {
        addPath("/opt/pi4j/lib/*");
        NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC");

        GpioController controller = GpioFactory.getInstance();
        GpioPinDigitalInput trigger = controller.provisionDigitalInputPin(RaspiPin.GPIO_02, "Trigger", PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput reset = controller.provisionDigitalInputPin(RaspiPin.GPIO_03, "Reset", PinPullResistance.PULL_DOWN);

        AudioInputStream audioInputStream0 = AudioSystem.getAudioInputStream(new File("audio1.wav").getAbsoluteFile());
        Clip clip1 = AudioSystem.getClip();
        clip1.open(audioInputStream0);

        AudioInputStream audioInputStream1 = AudioSystem.getAudioInputStream(new File("audio0.wav").getAbsoluteFile());
        Clip clip0 = AudioSystem.getClip();
        clip0.open(audioInputStream1);
        clip0.loop(Clip.LOOP_CONTINUOUSLY);

        JFrame frame = new JFrame();

        // Hide cursor
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        frame.getContentPane().setCursor(blankCursor);

        // Setup VLCJ
        ArrayList<String> args = new ArrayList<>();
        if (false) {
            args.add("--video-filter=rotate");
            args.add("--rotate-angle=180");
        }
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
        EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        Canvas vlcCanvas = new Canvas();
        vlcCanvas.setBackground(Color.black);
        frame.setLayout(new BorderLayout());
        frame.add(vlcCanvas, BorderLayout.CENTER);
        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(vlcCanvas));

        frame.setTitle("VideoProp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.stop();
                mediaPlayerFactory.release();
                mediaPlayer.release();
                System.exit(0);
            }
        });
        frame.getGraphicsConfiguration().getDevice().setFullScreenWindow(frame);
        frame.setPreferredSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getSize());
        frame.pack();
        frame.setVisible(true);

        mediaPlayer.setRepeat(true);
        clip0.start();
        mediaPlayer.playMedia("video0.mp4");
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventListener() {
            @Override
            public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {

            }

            @Override
            public void opening(MediaPlayer mediaPlayer) {

            }

            @Override
            public void buffering(MediaPlayer mediaPlayer, float newCache) {

            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {

            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {

            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {

            }

            @Override
            public void forward(MediaPlayer mediaPlayer) {

            }

            @Override
            public void backward(MediaPlayer mediaPlayer) {

            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                if (triggered)
                    mediaPlayer.playMedia(mediaPlayer.mrl().contains("video0.mp4") ? "Image.png" : "video0.mp4", mediaPlayer.mrl().contains("video0.mp4") ? "image-duration=10" : "");
                else
                    mediaPlayer.playMedia("video0.mp4");
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {

            }

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {

            }

            @Override
            public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {

            }

            @Override
            public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {

            }

            @Override
            public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {

            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {

            }

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {

            }

            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int newCount) {

            }

            @Override
            public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {

            }

            @Override
            public void elementaryStreamAdded(MediaPlayer mediaPlayer, int type, int id) {

            }

            @Override
            public void elementaryStreamDeleted(MediaPlayer mediaPlayer, int type, int id) {

            }

            @Override
            public void elementaryStreamSelected(MediaPlayer mediaPlayer, int type, int id) {

            }

            @Override
            public void error(MediaPlayer mediaPlayer) {

            }

            @Override
            public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {

            }

            @Override
            public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {

            }

            @Override
            public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {

            }

            @Override
            public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {

            }

            @Override
            public void mediaFreed(MediaPlayer mediaPlayer) {

            }

            @Override
            public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {

            }

            @Override
            public void mediaSubItemTreeAdded(MediaPlayer mediaPlayer, libvlc_media_t item) {

            }

            @Override
            public void newMedia(MediaPlayer mediaPlayer) {

            }

            @Override
            public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {

            }

            @Override
            public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {

            }

            @Override
            public void endOfSubItems(MediaPlayer mediaPlayer) {

            }
        });

        while (true) {
            if (triggered) {
                if (reset.getState().isHigh()) {
                    triggered = false;
                    mediaPlayer.playMedia("video0.mp4");
                }
            } else {
                if (trigger.getState().isHigh()) {
                    triggered = true;
                    mediaPlayer.playMedia("Image.png", "image-duration=10");
                    clip1.loop(2);
                }
            }
            Thread.sleep(10);
        }
    }

    public static void addPath(String s) throws Exception {
        File f = new File(s);
        URL u = f.toURL();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[]{u});
    }

    public static void main(String[] args) throws Exception {
        new VideoProp();
    }
}