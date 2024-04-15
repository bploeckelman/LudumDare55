package lando.systems.ld55.assets;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.ui.MovementBreadcrumb;

import java.util.List;

public class TileOverlayAssets {
    public static TextureRegion arrowUp;
    public static TextureRegion arrowDown;
    public static TextureRegion arrowLeft;
    public static TextureRegion arrowRight;
    public static TextureRegion arrowUpLeft;
    public static TextureRegion arrowUpRight;
    public static TextureRegion arrowDownLeft;
    public static TextureRegion arrowDownRight;
    public static TextureRegion arrowsCrossed;

    public static TextureRegion target;
    public static TextureRegion cross;
    public static TextureRegion checkmark;
    public static TextureRegion exclamation;
    public static TextureRegion question;
    public static TextureRegion pawns;
    public static TextureRegion pawnUp;
    public static TextureRegion pawnDown;
    public static TextureRegion tokenAdd;
    public static TextureRegion dot;
    public static TextureRegion skull;
    public static TextureRegion sword;
    public static TextureRegion crown;
    public static TextureRegion laurel;

    public static Array<TextureRegion> tags;

    public static NinePatch panelWhite;
    public static NinePatch panelRed;
    public static NinePatch panelGreen;
    public static NinePatch panelBlue;
    public static NinePatch panelYellow;

    public static void populate(TextureAtlas atlas) {
        arrowUp        = atlas.findRegion("icons/kenney-ui/arrowUp");
        arrowDown      = atlas.findRegion("icons/kenney-ui/arrowDown");
        arrowLeft      = atlas.findRegion("icons/kenney-ui/arrowLeft");
        arrowRight     = atlas.findRegion("icons/kenney-ui/arrowRight");
        arrowUpLeft    = atlas.findRegion("icons/kenney-ui/arrowUpLeft");
        arrowUpRight   = atlas.findRegion("icons/kenney-ui/arrowUpRight");
        arrowDownLeft  = atlas.findRegion("icons/kenney-ui/arrowDownLeft");
        arrowDownRight = atlas.findRegion("icons/kenney-ui/arrowDownRight");
        arrowsCrossed  = atlas.findRegion("icons/kenney-board-game/arrow_diagonal_cross_divided");

        target      = atlas.findRegion("icons/kenney-ui/target");
        cross       = atlas.findRegion("icons/kenney-ui/cross");
        checkmark   = atlas.findRegion("icons/kenney-ui/checkmark");
        exclamation = atlas.findRegion("icons/kenney-ui/exclamation");
        question    = atlas.findRegion("icons/kenney-board-game/dice_question");
        pawns       = atlas.findRegion("icons/kenney-board-game/pawns");
        pawnUp      = atlas.findRegion("icons/kenney-board-game/pawn_up");
        pawnDown    = atlas.findRegion("icons/kenney-board-game/pawn_down");
        tokenAdd    = atlas.findRegion("icons/kenney-board-game/token_add");
        dot         = atlas.findRegion("icons/kenney-board-game/tag_empty");
        skull       = atlas.findRegion("icons/kenney-board-game/skull");
        sword       = atlas.findRegion("icons/kenney-board-game/sword");
        crown       = atlas.findRegion("icons/kenney-board-game/crown_b");
        laurel      = atlas.findRegion("icons/kenney-board-game/award");

        tags = new Array<>();
        for (int i = 1; i <= 10; i++) {
            tags.add(atlas.findRegion("icons/kenney-board-game/tag_" + i));
        }

        var margin = 10;
        panelWhite  = new NinePatch(atlas.findRegion("icons/kenney-ui/grey_panel"), margin, margin, margin, margin);
        panelRed    = new NinePatch(atlas.findRegion("icons/kenney-ui/red_panel"), margin, margin, margin, margin);
        panelGreen  = new NinePatch(atlas.findRegion("icons/kenney-ui/green_panel"), margin, margin, margin, margin);
        panelBlue   = new NinePatch(atlas.findRegion("icons/kenney-ui/blue_panel"), margin, margin, margin, margin);
        panelYellow = new NinePatch(atlas.findRegion("icons/kenney-ui/yellow_panel"), margin, margin, margin, margin);
    }

    public static TextureRegion getArrowForDir(MovementBreadcrumb.Direction dir) {
        switch (dir) {
            case Up: return arrowUp;
            case Down: return arrowDown;
            case Left: return arrowLeft;
            case Right: return arrowRight;
            case UpLeft: return arrowUpLeft;
            case UpRight: return arrowUpRight;
            case DownLeft: return arrowDownLeft;
            case DownRight: return arrowDownRight;
            case End: return dot;
            default: return question;
        }
    }

    // testing... -----------------------------------------

    private static List<TextureRegion> arrows;
    public static TextureRegion getRandomArrow() {
        if (arrows == null) {
            arrows = List.of(arrowUp, arrowDown, arrowLeft, arrowRight,
                arrowUpLeft, arrowUpRight, arrowDownLeft, arrowDownRight);
        }
        var index = MathUtils.random(arrows.size() - 1);
        return arrows.get(index);
    }

    private static List<TextureRegion> regions;
    public static TextureRegion getRandomRegion() {
        if (regions == null) {
            regions = List.of(arrowUp, arrowDown, arrowLeft, arrowRight,
                arrowUpLeft, arrowUpRight, arrowDownLeft, arrowDownRight,
                target, cross, checkmark, exclamation);
        }
        var index = MathUtils.random(regions.size() - 1);
        return regions.get(index);
    }

    private static List<NinePatch> patches;
    public static NinePatch getRandomPatch() {
        if (patches == null) {
            patches = List.of(panelWhite, panelRed, panelGreen, panelBlue, panelYellow);
        }
        var index = MathUtils.random(patches.size() - 1);
        return patches.get(index);
    }
}
