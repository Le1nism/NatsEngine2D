package components;

import natsuki.GameObject;
import natsuki.MouseListener;
import natsuki.Window;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {

    GameObject holdingObject = null;

    public void pickupObject(GameObject go) {

        this.holdingObject = go;
        Window.getScene().addGameObjectToScene(go);
    }

    public void place() {

        this.holdingObject = null;
    }

    @Override
    public void editorUpdate(float dt) {

        if (holdingObject != null) {

            holdingObject.transform.position.x = MouseListener.getWorldX();
            holdingObject.transform.position.y = MouseListener.getWorldY();

            holdingObject.transform.position.x = ((int) Math.floor(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f;
            holdingObject.transform.position.y = ((int) Math.floor(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
                place();
        }
    }
}
