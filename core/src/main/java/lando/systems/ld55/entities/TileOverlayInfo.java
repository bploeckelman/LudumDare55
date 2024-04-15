package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class TileOverlayInfo {

    public static class Layer {
        public String name;
        public Color color;
        public Rectangle bounds;

        // NOTE - only *one* of these should be included per layer
        // NOTE - use 'icons/kenney-board-game/tag_#' for numbers
        public NinePatch patch;
        public TextureRegion region;
        public Animation<TextureRegion> anim;
        public float stateTime;

        public Layer(String name, Color color, Rectangle bounds, NinePatch patch, TextureRegion region, Animation<TextureRegion> anim) {
            this.name = name;
            this.color = color;
            this.bounds = bounds;
            this.patch = patch;
            this.region = region;
            this.anim = anim;
            this.stateTime = 0f;
        }
    }

    public final GameTile tile;
    public final int damageAmount;
    public final List<Layer> layers = new ArrayList<>();

    public TileOverlayInfo(GameTile tile, int damageAmount) {
        this.tile = tile;
        this.damageAmount = damageAmount;
    }

    public Layer findLayer(String name) {
        for (var layer : layers) {
            if (layer.name.equals(name)) {
                return layer;
            }
        }
        return null;
    }

    public int findLayerIndex(String name) {
        for (int i = 0; i < layers.size(); i++) {
            var layer = layers.get(i);
            if (layer.name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public Layer createLayer(String name, float fillPercent, float r, float g, float b, float a, NinePatch patch, TextureRegion region, Animation<TextureRegion> anim) {
        var fillW = fillPercent * tile.bounds.width;
        var fillH = fillPercent * tile.bounds.height;
        var offsetX = (tile.bounds.width - fillW) / 2f;
        var offsetY = (tile.bounds.height - fillH) / 2f;
        var bounds = new Rectangle(
            tile.bounds.x + offsetX,
            tile.bounds.y + offsetY,
            fillW, fillH);
        return new Layer(name, new Color(r, g, b, a), bounds, patch, region, anim);
    }

    public TileOverlayInfo insertLayerBefore(String afterLayerName, String thisLayerName, float fillPercent, Color color, NinePatch patch, TextureRegion region, Animation<TextureRegion> anim) {
        var targetIndex = findLayerIndex(afterLayerName);
        if (targetIndex == -1) {
            return addLayer(thisLayerName, fillPercent, color, patch, region, anim);
        }
        var layer = createLayer(thisLayerName, fillPercent, color.r, color.g, color.b, color.a, patch, region, anim);
        // shifts existing layer to the 'right'
        layers.add(targetIndex, layer);
        return this;
    }

    public TileOverlayInfo insertLayerAfter(String prevLayerName, String thisLayerName, float fillPercent, Color color, NinePatch patch, TextureRegion region, Animation<TextureRegion> anim) {
        var targetIndex = findLayerIndex(prevLayerName);
        if (targetIndex == -1) {
            return addLayer(thisLayerName, fillPercent, color, patch, region, anim);
        }

        var layer = createLayer(thisLayerName, fillPercent, color.r, color.g, color.b, color.a, patch, region, anim);

        // since add(index, item) moves existing elements to the 'right'
        // to add something 'after' an existing item we need to add at the *next* index
        targetIndex += 1;
        if (targetIndex >= layers.size()) {
            // add to the end (add(index, item) throws if index is out of bounds)
            layers.add(layer);
        } else {
            layers.add(targetIndex, layer);
        }
        return this;
    }

    public TileOverlayInfo addLayer(String name, float fillPercent, Color color, NinePatch patch, TextureRegion region, Animation<TextureRegion> anim) {
        return addLayer(name, fillPercent, color.r, color.g, color.b, color.a, patch, region, anim);
    }

    public TileOverlayInfo addLayer(String name, float fillPercent, Color color, float alpha, NinePatch patch, TextureRegion region, Animation<TextureRegion> anim) {
        return addLayer(name, fillPercent, color.r, color.g, color.b, alpha, patch, region, anim);
    }

    public TileOverlayInfo addLayer(String name, float fillPercent, float r, float g, float b, float a, NinePatch patch, TextureRegion region, Animation<TextureRegion> anim) {
        var fillW = fillPercent * tile.bounds.width;
        var fillH = fillPercent * tile.bounds.height;
        var offsetX = (tile.bounds.width - fillW) / 2f;
        var offsetY = (tile.bounds.height - fillH) / 2f;
        var bounds = new Rectangle(
            tile.bounds.x + offsetX,
            tile.bounds.y + offsetY,
            fillW, fillH);

        layers.add(new Layer(name, new Color(r, g, b, a), bounds, patch, region, anim));
        return this;
    }

    public void update(float dt) {
        for (var layer : layers) {
            layer.stateTime += dt;
        }
    }

    public void render(SpriteBatch batch) {
        var prevColor = batch.getColor();
        for (var layer : layers) {
            batch.setColor(layer.color);
            if (layer.patch != null) {
                layer.patch.draw(batch, layer.bounds.x, layer.bounds.y, layer.bounds.width, layer.bounds.height);
            }
            if (layer.region != null) {
                batch.draw(layer.region, layer.bounds.x, layer.bounds.y, layer.bounds.width, layer.bounds.height);
            }
            if (layer.anim != null) {
                var keyframe = layer.anim.getKeyFrame(layer.stateTime);
                batch.draw(keyframe, layer.bounds.x, layer.bounds.y, layer.bounds.width, layer.bounds.height);
            }
        }
        batch.setColor(prevColor);
    }
}
