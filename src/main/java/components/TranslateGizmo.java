package components;

import editor.PropertiesWindow;
import natsuki.MouseListener;

public class TranslateGizmo extends Gizmo {

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {

        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {

        if (activeGameObject != null) {

            if (xAxisActive && !yAxisActive) {

                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            } else if (yAxisActive && !xAxisActive) {

                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
        }

        super.editorUpdate(dt);
    }
}
