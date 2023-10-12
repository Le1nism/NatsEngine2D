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

    public LevelEditorScene() {

    }

    @Override
    public void init() {

        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());

        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");

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

        for (GameObject go : this.gameObjects)
            go.update(dt);
    }

    @Override
    public void render() {

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
