package renderer;

import java.util.ArrayList;
import java.util.List;

import components.SpriteRenderer;
import natsuki.GameObject;

public class Renderer {
    
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {

        this.batches = new ArrayList<>();
    }

    public void add(GameObject go) {

        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);

        if (spr != null)
            add(spr);
    }

    private void add(SpriteRenderer sprite) {

        boolean added = false;

        for (RenderBatch batch : batches) {

            if (batch.hasRoom()) {

                Texture tex = sprite.getTexture();

                if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {

                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {

            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
        }
    }

    public void render() {

        for (RenderBatch batch : batches)
            batch.render();
    }
}
