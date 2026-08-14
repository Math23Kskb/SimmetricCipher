package chat.cipher;

public class VigenereCipher implements Cipher {
    private final String key;

    public VigenereCipher(String skey){
        key = skey;
    }

    @Override
    public String encrypt(String text){
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++){
            char charText = text.charAt(i);
            char charKey = key.charAt(i % key.length());
            int sumOfChars = (charText + charKey) % 26;
            
            char encryptedChar = (char)(sumOfChars + 'A');
            encryptedText.append(encryptedChar);
        }

        return encryptedText.toString();
    }

    @Override
    public String decrypt(String text){
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++){
            char charText = text.charAt(i);
            char charKey = key.charAt(i % key.length());
            int sumOfChars = (charText - charKey + 26) % 26;
            
            char encryptedChar = (char)(sumOfChars + 'A');
            decryptedText.append(encryptedChar);
        }

        return decryptedText.toString();
    }

}
