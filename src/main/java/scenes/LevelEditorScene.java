package scenes;

import org.joml.Vector2f;
import org.joml.Vector3f;

import components.GridLines;
import components.MouseControls;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;

import imgui.ImGui;
import imgui.ImVec2;
import natsuki.Camera;
import natsuki.GameObject;
import natsuki.Prefabs;
import natsuki.Transform;
import physics2d.PhysicsSystem2D;
import physics2d.rigidbody.Rigidbody2D;
import renderer.DebugDraw;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    private Spritesheet sprites;

    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);
    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -10));
    Transform obj1, obj2;
    Rigidbody2D rb1, rb2;

    public LevelEditorScene() {

    }

    @Override
    public void init() {

        levelEditorStuff.addComponent(new MouseControls());
        // levelEditorStuff.addComponent(new GridLines());

        obj1 = new Transform(new Vector2f(100, 500));
        obj2 = new Transform(new Vector2f(200, 500));

        rb1 = new Rigidbody2D();
        rb2 = new Rigidbody2D();

        rb1.setRawTransform(obj1);
        rb2.setRawTransform(obj2);

        rb1.setMass(100.0f);
        rb2.setMass(200.0f);

        physics.addRigidbody(rb1);
        physics.addRigidbody(rb2);

        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");

        if (levelLoaded)
            if (gameObjects.size() > 0)
                this.activeGameObject = gameObjects.get(0);

        return;
    }

    private void loadResources() {

        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png", new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"), 16, 16, 81, 0));
        AssetPool.getTexture("assets/images/nat1.png");

        for(GameObject g : gameObjects)
            if (g.getComponent(SpriteRenderer.class) != null) {

                    SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                    if (spr.getTexture() != null)
                        spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
            }

    }

    @Override
    public void update(float dt) {

        levelEditorStuff.update(dt);

        for (GameObject go : this.gameObjects) {

            go.update(dt);
        }

        DebugDraw.addBox2D(obj1.position, new Vector2f(32, 32), 0.0f, new Vector3f(1, 0, 0));
        DebugDraw.addBox2D(obj2.position, new Vector2f(32, 32), 0.0f, new Vector3f(0.2f, 0.8f, 0.1f));
        physics.update(dt);

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
