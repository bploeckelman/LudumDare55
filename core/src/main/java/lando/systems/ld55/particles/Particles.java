package lando.systems.ld55.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import lando.systems.ld55.assets.Assets;

public class Particles implements Disposable {

    public enum Layer { BACKGROUND, FOREGROUND }

    private static final int MAX_PARTICLES = 4000;

    private final Assets assets;
    private final ObjectMap<Layer, Array<Particle>> activeParticles;
    private final Pool<Particle> particlePool = Pools.get(Particle.class, MAX_PARTICLES);

    public Particles(Assets assets) {
        this.assets = assets;
        this.activeParticles = new ObjectMap<>();
        int particlesPerLayer = MAX_PARTICLES / Layer.values().length;
        this.activeParticles.put(Layer.BACKGROUND, new Array<>(false, particlesPerLayer));
        this.activeParticles.put(Layer.FOREGROUND,     new Array<>(false, particlesPerLayer));
    }

    public void clear() {
        for (Layer layer : Layer.values()) {
            particlePool.freeAll(activeParticles.get(layer));
            activeParticles.get(layer).clear();
        }
    }

    public void update(float dt) {
        for (Layer layer : Layer.values()) {
            for (int i = activeParticles.get(layer).size - 1; i >= 0; --i) {
                Particle particle = activeParticles.get(layer).get(i);
                particle.update(dt);
                if (particle.isDead()) {
                    activeParticles.get(layer).removeIndex(i);
                    particlePool.free(particle);
                }
            }
        }
    }


    public void draw(SpriteBatch batch, Layer layer) {
        activeParticles.get(layer).forEach(particle -> particle.render(batch));
    }

    @Override
    public void dispose() {
        clear();
    }

    // ------------------------------------------------------------------------
    // Helper fields for particle spawner methods
    // ------------------------------------------------------------------------
    private final Color tempColor = new Color();
    private final Vector2 tempVec2 = new Vector2();

    // ------------------------------------------------------------------------
    // Spawners for different particle effects
    // ------------------------------------------------------------------------

    public void smoke(float inX, float inY) {
        for (int i = 0; i < 30; i++) {
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(0f, 100f);
            float x = inX + MathUtils.random(-100f, 100f);
            float y = inY + MathUtils.random(-100f, 100f);
            float size = MathUtils.random(60f, 200f);
            float color = MathUtils.random(.3f, 1f);
            activeParticles.get(Layer.FOREGROUND).add(Particle.initializer(particlePool.obtain())
                .keyframe(assets.particles.smoke)
                .startPos(x, y)
                .velocity(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed)
                .startColor(color, color, color, 1f)
                .endColor(0, 0, 0, 0)
                .startSize(size)
                .endSize(5f)
                .timeToLive(MathUtils.random(2f, 4f))
                .init()
            );
        }
    }

    public void tinySmoke(float inX, float inY) {
        for (int i = 0; i < 5; i++) {
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(0f, 10f);
            float x = inX + MathUtils.random(-10f, 10f);
            float y = inY + MathUtils.random(-10f, 10f);
            float size = MathUtils.random(5f, 20f);
            float color = MathUtils.random(.3f, 1f);
            activeParticles.get(Layer.FOREGROUND).add(Particle.initializer(particlePool.obtain())
                .keyframe(assets.particles.smoke)
                .startPos(x, y)
                .velocity(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed)
                .startColor(color, color, color, 1f)
                .endColor(0, 0, 0, 0)
                .startSize(size)
                .endSize(5f)
                .timeToLive(MathUtils.random(2f, 4f))
                .init()
            );
        }
    }

    public void levelUpEffect(float x, float y) {
        // Stars
        for (int i = 0; i < 30; i++) {
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(50f, 150f);

            activeParticles.get(Layer.FOREGROUND).add(Particle.initializer(particlePool.obtain())
                .keyframe(assets.particles.stars.getKeyFrame(MathUtils.random(0f, 1f)))
                .startPos(x, y)
                .velocityDirection(angle, speed)
                .startColor(1f, 1f, .8f, 1f)
                .endColor(1f, 1f, .8f, 0f)
                .startSize(MathUtils.random(50f, 80f))
                .endSize(0f)
                .timeToLive(MathUtils.random(1f, 2f))
                .init()
            );
        }
    }

    public void portal(float x, float y, float radius) {
        for (int i = 0; i < 50; i++) {
            float angle = MathUtils.random(0f, 360f);
            float distance = MathUtils.random(50f, 100f);
            float offsetX = MathUtils.cosDeg(angle) * distance;
            float offsetY = MathUtils.sinDeg(angle) * distance;
            float targetX = x;
            float targetY = y;

            activeParticles.get(Layer.FOREGROUND).add(Particle.initializer(particlePool.obtain())
                .keyframe(assets.particles.stars.getKeyFrame(MathUtils.random(0f, 1f)))
                .startPos(x + offsetX, y + offsetY)
                .targetPos(targetX, targetY)  // Target the portal's center
                .startColor(.5f, .5f, 1f, 1f)
                .endColor(.9f, .9f, 1f, 0f)
                .startSize(MathUtils.random(25f, 50f))
                .endSize(0f)
                .timeToLive(MathUtils.random(2f, 4f))
                .init()
            );
        }

        for (int i = 0; i < 10; i++) {
            float angle = MathUtils.random(0f, 360f);
            float distance = radius + MathUtils.random(-10f, 10f);
            float offsetX = MathUtils.cosDeg(angle) * distance;
            float offsetY = MathUtils.sinDeg(angle) * distance;

            activeParticles.get(Layer.FOREGROUND).add(Particle.initializer(particlePool.obtain())
                .keyframe(assets.particles.twirls.getKeyFrame(MathUtils.random(0f, 1f)))
                .startPos(x + offsetX, y + offsetY)
                .velocity(MathUtils.cosDeg(angle) * 10f, MathUtils.sinDeg(angle) * 10f)
                .startColor(0.5f, 0.5f, 1f, 0.5f)
                .endColor(0.8f, 0.8f, 1f, 0f)
                .startSize(MathUtils.random(25f, 50f))
                .endSize(0f)
                .startRotation(0f)
                .endRotation(MathUtils.random(360, 1440))
                .timeToLive(MathUtils.random(1f, 3f)) // Short lifespan
                .init()
            );
        }
    }

}
