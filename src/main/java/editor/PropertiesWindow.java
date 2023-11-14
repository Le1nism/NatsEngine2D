package editor;

import imgui.ImGui;
import natsuki.GameObject;
import natsuki.MouseListener;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import components.NonPickable;

public class PropertiesWindow {

    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture) {

        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {

        debounce -= dt;

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {

                int x = (int) MouseListener.getScreenX();
                int y = (int) MouseListener.getScreenY();

                int gameObjectID = pickingTexture.readPixel(x, y);
                GameObject pickedObj = currentScene.getGameObject(gameObjectID);

                if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null)
                    activeGameObject = pickedObj;
                else if (pickedObj == null && !MouseListener.isDragging())
                    activeGameObject = null;

                this.debounce = 0.2f;
            }
    }

    public void imGui() {

        if (activeGameObject != null) {

            ImGui.begin("Properties");
            activeGameObject.imGui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {

        return this.activeGameObject;
    }
}
