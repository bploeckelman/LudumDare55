package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld55.Main;
import lando.systems.ld55.assets.TileOverlayAssets;

public class RadialCloseButton extends RadialButton{

    RadialMenu menu;

    public RadialCloseButton(RadialMenu menu) {
        super(TileOverlayAssets.panelWhite, TileOverlayAssets.cardX, "Close\nMenu", true);
        this.menu = menu;
    }
    @Override
    public void onClick() {
        menu.exitMenu();
    }
}
