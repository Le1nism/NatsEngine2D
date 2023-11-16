package editor;

import imgui.ImGui;
import natsuki.GameObject;
import natsuki.MouseListener;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.Rigidbody2D;
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

            if (ImGui.beginPopupContextWindow("ComponentAdder")) {

                if (ImGui.menuItem("Add Rigidbody")) 
                    if (activeGameObject.getComponent(Rigidbody2D.class) == null) 
                        activeGameObject.addComponent(new Rigidbody2D());

                if (ImGui.menuItem("Add Box Collider")) 
                    if (activeGameObject.getComponent(Box2DCollider.class) == null && activeGameObject.getComponent(CircleCollider.class) == null) 
                        activeGameObject.addComponent(new Box2DCollider());

                if (ImGui.menuItem("Add Circle Collider")) 
                    if (activeGameObject.getComponent(CircleCollider.class) == null && activeGameObject.getComponent(Box2DCollider.class) == null) 
                        activeGameObject.addComponent(new CircleCollider());

                ImGui.endPopup();
            }

            activeGameObject.imGui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {

        return this.activeGameObject;
    }

    public void setActiveGameObject(GameObject go) {

        this.activeGameObject = go;
    }
}
