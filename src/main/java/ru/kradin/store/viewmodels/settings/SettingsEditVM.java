package ru.kradin.store.viewmodels.settings;

import org.zkoss.bind.annotation.*;
import org.zkoss.zul.Window;

public class SettingsEditVM {
    private Window window;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.window = window;
    }

    @Command("close")
    public void close() {
        window.detach();
    }
}
