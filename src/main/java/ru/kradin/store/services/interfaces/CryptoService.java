package ru.kradin.store.services.interfaces;

public interface CryptoService {
    public String encrypt(String data) throws Exception;

    public String decrypt(String data) throws Exception;
}
