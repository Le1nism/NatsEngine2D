package editor;

import imgui.ImGui;
import natsuki.GameObject;
import natsuki.MouseListener;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {

    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture) {

        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {

                int x = (int) MouseListener.getScreenX();
                int y = (int) MouseListener.getScreenY();

                System.out.println(pickingTexture.readPixel(x, y));
            }
    }

    public void imGui() {

        if (activeGameObject != null) {

            ImGui.begin("Inspector");
            activeGameObject.imGui();
            ImGui.end();
        }
    }
}