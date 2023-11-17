package editor;

import java.util.List;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import natsuki.GameObject;
import natsuki.Window;

public class SceneHierarchyWindow {

    public void imGui() {

        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();

        int index = 0;
        for (GameObject obj : gameObjects) {

            if (!obj.doSerialization()) continue;

            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(obj.name, ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth, obj.name);
            ImGui.popID();

            if (treeNodeOpen) ImGui.treePop();

            index++;
        }

        ImGui.end();
    }
}
