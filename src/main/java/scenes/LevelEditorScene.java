package scenes;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.GridLines;
import components.MouseControls;
import components.Rigidbody;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import imgui.ImVec2;
import natsuki.Camera;
import natsuki.GameObject;
import natsuki.Prefabs;
import natsuki.Transform;
import renderer.DebugDraw;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;
    private SpriteRenderer obj1SpriteRenderer;

    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditorScene() {

    }

    @Override
    public void init() {

        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());

        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");

        if (levelLoaded) {

            this.activeGameObject = gameObjects.get(0);
            return;
        }

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(300, 100), new Vector2f(256, 256)), 2);

        obj1SpriteRenderer = new SpriteRenderer();
        obj1SpriteRenderer = new SpriteRenderer();
        obj1SpriteRenderer.setColor(new Vector4f(1, 0, 0, 1));
        obj1.addComponent(obj1SpriteRenderer);
        obj1.addComponent(new Rigidbody());
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);

        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/nat1.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {

        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png", new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"), 16, 16, 81, 0));
        AssetPool.getTexture("assets/images/nat1.png");
    }

    float t = 0.0f;
    @Override
    public void update(float dt) {

        float x = ((float) Math.sin(t) * 200.0f) + 600;
        float y = ((float) Math.cos(t) * 200.0f) + 400;
        t += 0.05f;

        DebugDraw.addLine2D(new Vector2f(600, 400), new Vector2f(x, y), new Vector3f(0, 0, 1));

        levelEditorStuff.update(dt);

        for (GameObject go : this.gameObjects) {

            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imGui() {

        ImGui.begin("Natsuki Window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;

        for (int i = 0; i < sprites.size(); i++) {

            Sprite sprite = sprites.getSprite(i);

            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);

            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {

                GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);

                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }

            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);

            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if (i + 1 < sprites.size() && nextButtonX2 < windowX2)
                ImGui.sameLine();
        }

        ImGui.end();
    }
}
