package chat.cipher;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Rc4Cipher implements Cipher {

    private final byte[] key;

    public Rc4Cipher(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("A chave não pode estar vazia.");
        }
        this.key = key.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String encrypt(String text) {
        byte[] plainBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] cipherBytes = process(plainBytes);

        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    @Override
    public String decrypt(String text) {
        byte[] cipherBytes = Base64.getDecoder().decode(text);
        byte[] plainBytes = process(cipherBytes);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }


    private byte[] process(byte[] input) {
        int[] s = ksa();
        byte[] output = new byte[input.length];

        int i = 0, j = 0;
        for (int n = 0; n < input.length; n++) {
            i = (i + 1) % 256;
            j = (j + s[i]) % 256;

            int temp = s[i];
            s[i] = s[j];
            s[j] = temp;

            int k = s[(s[i] + s[j]) % 256];
            output[n] = (byte) (input[n] ^ k);
        }
        return output;
    }

    private int[] ksa() {
        int[] s = new int[256];
        for (int i = 0; i < 256; i++) s[i] = i;

        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + s[i] + (key[i % key.length] & 0xFF)) % 256;

            int temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
        return s;
    }
}