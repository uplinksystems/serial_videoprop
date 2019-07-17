package escreens;

import com.pi4j.io.gpio.*;
import com.sun.javafx.logging.JFRInputEvent;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import javax.imageio.ImageIO;
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

    private VideoProp() throws Exception {
        addPath("/opt/pi4j/lib/*");
        NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC");

        GpioController controller = GpioFactory.getInstance();
        GpioPinDigitalInput trigger = controller.provisionDigitalInputPin(RaspiPin.GPIO_02, "Trigger", PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput reset = controller.provisionDigitalInputPin(RaspiPin.GPIO_03, "Reset", PinPullResistance.PULL_DOWN);

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
        mediaPlayer.playMedia("video0.mp4");

        boolean triggered = false;
        while (true) {
            if(triggered) {
                if (reset.getState().isHigh()) {
                    triggered = false;
                    mediaPlayer.playMedia("video0.mp4");
                }
            } else {
                if (trigger.getState().isHigh()) {
                    triggered = true;
                    mediaPlayer.playMedia("video1.mp4");
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