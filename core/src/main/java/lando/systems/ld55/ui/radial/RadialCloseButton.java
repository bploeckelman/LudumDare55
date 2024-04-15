package lando.systems.ld55.ui.radial;

import lando.systems.ld55.assets.TileOverlayAssets;

public class RadialCloseButton extends RadialButton{

    RadialMenu menu;

    public RadialCloseButton(RadialMenu menu) {
        super(TileOverlayAssets.panelWhite,
            TileOverlayAssets.disabledCircle,
            "Close\nMenu",
            true);
        this.menu = menu;
        this.backgroundHovered = TileOverlayAssets.panelYellow;
    }
    @Override
    public void onClick() {
        menu.exitMenu();
    }
}
