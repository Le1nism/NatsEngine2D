package components;

import editor.PropertiesWindow;
import natsuki.MouseListener;

public class ScaleGizmo extends Gizmo {
    
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {

        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {

        if (activeGameObject != null) {

            if (xAxisActive && !yAxisActive) {

                activeGameObject.transform.scale.x -= MouseListener.getWorldX();
            } else if (yAxisActive && !xAxisActive) {

                activeGameObject.transform.scale.y -= MouseListener.getWorldY();
            }
        }

        super.editorUpdate(dt);
    }
}
