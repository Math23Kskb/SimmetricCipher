package chat.cipher;

public class NoCipher implements Cipher {
    @Override
    public String encrypt(String text) {
        return text;
    }

    @Override
    public String decrypt(String text) {
        return text;
    }
}
