package lando.systems.ld55.teavm;

import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;

/**
 * Launches the TeaVM/HTML application.
 */
public class TeaVMLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        // change these to both 0 to use all available space, or both -1 for the canvas size.
        config.width = Config.Screen.window_width;
        config.height = Config.Screen.window_height;
        config.alpha = true;
        config.useDebugGL = true;
        new TeaApplication(new Main(), config);
    }
}
