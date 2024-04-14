package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld55.Main;

public class RadialCloseButton extends RadialButton{

    RadialMenu menu;

    public RadialCloseButton(RadialMenu menu) {
        super(Main.game.assets.closeButton, "Close Menu");
        this.menu = menu;
    }
    @Override
    public void onClick() {
        menu.exitMenu();
    }
}
