package ru.kradin.store.tasks;

import ru.kradin.store.utils.CryptoUtil;

import java.util.TimerTask;

public class SecureTask extends TimerTask {

    public SecureTask() {
        CryptoUtil.generateNewKeyAndShiftAmount();
    }

    @Override
    public void run() {
        CryptoUtil.generateNewKeyAndShiftAmount();
    }
}
