package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TileOverlay {

    public GameTile tile;

    // different things that could be drawn or impact the drawing, not all are required
    public Color color;
    public NinePatch patch;
    public Animation<TextureRegion> anim;
    public NinePatch outlinePatch;
    public Animation<TextureRegion> outlineAnim;
    public Rectangle bounds;

}
