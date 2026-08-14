package chat.cipher;

public class CaesarCipher implements Cipher {
    private final int key;

    public CaesarCipher(int skey){
        key = skey;
    }

    @Override
    public String encrypt(String text){
        StringBuilder encryptedText = new StringBuilder();

        for (char character : text.toCharArray()){
            char encryptedChar = (char)((character - 'A' + Math.floorMod(key,26))%26 +'A');
            encryptedText.append(encryptedChar);
        }

        return encryptedText.toString();

    }

    @Override
    public String decrypt(String text){
        StringBuilder decryptedText = new StringBuilder();

        for (char character : text.toCharArray()){
            char decryptedChar = (char)(((character - 'A' - Math.floorMod(key,26)) + 26 )%26 + 'A');
            decryptedText.append(decryptedChar);
        }

        return decryptedText.toString();
    }
}
