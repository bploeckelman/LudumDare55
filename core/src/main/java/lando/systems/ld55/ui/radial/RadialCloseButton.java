package lando.systems.ld55.ui.radial;

import lando.systems.ld55.assets.TileOverlayAssets;

public class RadialCloseButton extends RadialButton{

    RadialMenu menu;

    public RadialCloseButton(RadialMenu menu) {
        super(TileOverlayAssets.panelRed,
            TileOverlayAssets.arrowBack,
            "", //"Close\nMenu",
            true);
        this.menu = menu;
        this.iconRadiusScale = 0.5f;
//        this.iconOffsetX = 10f;
//        this.iconOffsetY = 10f;
        this.iconEnabledColor.set(1, 1, 1, 1);
        this.backgroundHovered = TileOverlayAssets.panelYellow;
    }
    @Override
    public void onClick() {
        menu.exitMenu();
    }
}
