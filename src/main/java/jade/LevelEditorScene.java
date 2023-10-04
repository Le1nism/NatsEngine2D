package jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {

        this.camera = new Camera(new Vector2f(-250, 0));

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/nat1.png")));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 400), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/nat2.png")));
        this.addGameObjectToScene(obj2);

        loadResources();
    }

    private void loadResources() {

        AssetPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {

        System.out.println("FPS > " + (1 / dt) );

        for (GameObject go : this.gameObjects) {

            go.update(dt);
        }

        this.renderer.render();
    }
}
