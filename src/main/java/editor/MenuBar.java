package editor;

import imgui.ImGui;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;

public class MenuBar {

    public void imGui() {

        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")) {

            if (ImGui.menuItem("Save", "Ctrl+S")) {

                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Open", "Ctrl+O")) {

                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}
