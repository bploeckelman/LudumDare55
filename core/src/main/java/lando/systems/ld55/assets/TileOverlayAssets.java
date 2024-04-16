package lando.systems.ld55.assets;

import com.badlogic.gdx.graphics.Color;
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
    public static TextureRegion arrowBack;

    public static TextureRegion target;
    public static TextureRegion cross;
    public static TextureRegion plus;
    public static TextureRegion checkmark;
    public static TextureRegion exclamation;
    public static TextureRegion question;
    public static TextureRegion dollar;
    public static TextureRegion dollarOutline;
    public static TextureRegion disabledCross;
    public static TextureRegion disabledCircle;
    public static TextureRegion pawn;
    public static TextureRegion pawns;
    public static TextureRegion pawnUp;
    public static TextureRegion pawnDown;
    public static TextureRegion pawnRight;
    public static TextureRegion pawnPlus;
    public static TextureRegion tokenAdd;
    public static TextureRegion dot;
    public static TextureRegion skull;
    public static TextureRegion sword;
    public static TextureRegion crown;
    public static TextureRegion laurel;
    public static TextureRegion cardX;

    public static Array<TextureRegion> tags;
    public static Array<TextureRegion> numbers;
    public static Array<TextureRegion> numbersOutline;

    public static NinePatch panelWhite;
    public static NinePatch panelRed;
    public static NinePatch panelOrange;
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
        arrowBack      = atlas.findRegion("icons/kenney-board-game/arrow_counterclockwise_outline");

        target      = atlas.findRegion("icons/kenney-ui/target");
        cross       = atlas.findRegion("icons/kenney-ui/cross");
        plus        = atlas.findRegion("icons/kenney-ui/plus");
        checkmark   = atlas.findRegion("icons/kenney-ui/checkmark");
        exclamation = atlas.findRegion("icons/kenney-ui/exclamation");
        question    = atlas.findRegion("icons/kenney-board-game/dice_question");
        dollar      = atlas.findRegion("icons/kenney-board-game/dollar");
        dollarOutline  = atlas.findRegion("icons/kenney-board-game/dollar_outline");
        disabledCross  = atlas.findRegion("icons/kenney-board-game/flair_small_cross_outline2");
        disabledCircle = atlas.findRegion("icons/kenney-board-game/flair_small_disabled_outline2");
        pawn        = atlas.findRegion("icons/kenney-board-game/pawn");
        pawns       = atlas.findRegion("icons/kenney-board-game/pawns");
        pawnUp      = atlas.findRegion("icons/kenney-board-game/pawn_up");
        pawnDown    = atlas.findRegion("icons/kenney-board-game/pawn_down");
        pawnRight   = atlas.findRegion("icons/kenney-board-game/pawn_right");
        pawnPlus    = atlas.findRegion("icons/kenney-board-game/pawn_plus");
        tokenAdd    = atlas.findRegion("icons/kenney-board-game/token_add");
        dot         = atlas.findRegion("icons/kenney-board-game/tag_empty");
        skull       = atlas.findRegion("icons/kenney-board-game/skull");
        sword       = atlas.findRegion("icons/kenney-board-game/sword");
        crown       = atlas.findRegion("icons/kenney-board-game/crown_b");
        laurel      = atlas.findRegion("icons/kenney-board-game/award");
        cardX       = atlas.findRegion("icons/kenney-board-game/card_outline_remove");

        tags = new Array<>();
        for (int i = 0; i <= 10; i++) {
            var tag = atlas.findRegion("icons/kenney-board-game/tag" + i);
            tags.add(tag);
        }
        numbers = new Array<>();
        numbersOutline = new Array<>();
        for (int i = 0; i < 10; i++) {
            var number = atlas.findRegion("icons/kenney-board-game/flair_number" + i);
            var numberOutline = atlas.findRegion("icons/kenney-board-game/flair_number_" + i + "_outline");
            numbers.add(number);
            numbersOutline.add(numberOutline);
        }

        var margin = 10;
        panelWhite  = new NinePatch(atlas.findRegion("icons/kenney-ui/grey_panel"), margin, margin, margin, margin);
        panelRed    = new NinePatch(atlas.findRegion("icons/kenney-ui/red_panel"), margin, margin, margin, margin);
        panelOrange = new NinePatch(atlas.findRegion("icons/kenney-ui/orange_panel"), margin, margin, margin, margin);
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

    public static NinePatch getPatchForDamageAmount(int damage) {
        if      (damage >= 3) return panelRed;
        else if (damage >  1) return panelOrange;
        else if (damage >  0) return panelYellow;
        else                  return panelWhite;
    }

    public static Color getColorForDamageAmount(int damage) {
        if      (damage >= 3) return Color.RED;
        else if (damage >  1) return Color.ORANGE;
        else if (damage >  0) return Color.YELLOW;
        else                  return Color.WHITE;
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
