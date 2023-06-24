package ru.kradin.store.utils;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CryptoUtil {
    private static final String PATH_TO_PROPERTIES = "src/main/resources/application.properties";
    @Value("${security.key}")
    private static String key;
    @Value("${security.shiftAmount}")
    private static int shift_amount;

    public static String encrypt(String data) throws Exception {
        int ivLength = ThreadLocalRandom.current().nextInt(96, 255); // генерируем случайную длину IV в диапазоне от 12 до 64
        byte[] iv = generateIV(ivLength); // создаем массив IV заданной длины
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), iv, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, gcmParameterSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        byte[] encryptedWithIV = new byte[iv.length + encrypted.length + 4];
        ByteBuffer buffer = ByteBuffer.wrap(encryptedWithIV);
        buffer.putInt(ivLength+shift_amount);
        buffer.put(iv);
        buffer.put(encrypted);
        return Base64.getEncoder().encodeToString(encryptedWithIV);
    }

    public static String decrypt(String data) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(data);
        ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
        int ivLength = buffer.getInt()-shift_amount;
        byte[] iv = new byte[ivLength];
        buffer.get(iv);
        byte[] encrypted = new byte[encryptedData.length - 4 - ivLength];
        buffer.get(encrypted);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), iv, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secret, gcmParameterSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public static void generateNewKeyAndShiftAmount() {
        SecureRandom secureRandom;

        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            return;
        }

        int length = ThreadLocalRandom.current().nextInt(128, 256);
        byte[] keyBytes = new byte[length];
        secureRandom.nextBytes(keyBytes);
        key = Base64.getEncoder().encodeToString(keyBytes);

        shift_amount = ThreadLocalRandom.current().nextInt(524, 65536);
        if (secureRandom.nextBoolean())
            shift_amount = -shift_amount;

        try {
            updateKeyAndShiftAmount(key,shift_amount);
        } catch (IOException e) {
            System.out.println("It's time to cry");
        }
    }

    private static byte[] generateIV(int length) throws NoSuchAlgorithmException {
        byte[] iv = new byte[length];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        return iv;
    }

    private static void updateKeyAndShiftAmount(String newKey, int newShiftAmount) throws IOException {
        Path path = Paths.get(PATH_TO_PROPERTIES);
        List<String> lines = Files.readAllLines(path);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("security.key=")) {
                lines.set(i, "security.key=" + newKey);
            } else if (line.startsWith("security.shiftAmount=")) {
                lines.set(i, "security.shiftAmount=" + newShiftAmount);
            }
        }
        Files.write(path, lines);
    }
}
