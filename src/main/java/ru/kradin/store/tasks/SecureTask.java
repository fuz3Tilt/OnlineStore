package ru.kradin.store.tasks;

import ru.kradin.store.services.interfaces.CryptoSettingsService;

import java.util.TimerTask;

public class SecureTask extends TimerTask {

    private CryptoSettingsService cryptoSettingsService;

    public SecureTask(CryptoSettingsService cryptoSettingsService) {
        this.cryptoSettingsService = cryptoSettingsService;
        cryptoSettingsService.generateNewKeyAndShiftAmount();
    }

    @Override
    public void run() {
        cryptoSettingsService.generateNewKeyAndShiftAmount();
    }
}
