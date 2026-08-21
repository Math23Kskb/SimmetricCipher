package chat.cipher;

public class VigenereCipher implements Cipher {
    private final String key;
    private ProcessText textPocessor = new ProcessText();

    public VigenereCipher(String skey){
        skey = textPocessor.removeAccent(skey.toUpperCase());
        textPocessor.checkVigenere(skey);
        key = skey;
    }

    @Override
    public String encrypt(String text){
        int indexOfKey = 0;
        StringBuilder encryptedText = new StringBuilder();

        text = text.toUpperCase();
        text = textPocessor.removeAccent(text);

        for (int i = 0; i < text.length(); i++){
            if(text.charAt(i) < 'A' || text.charAt(i) > 'Z'){
                encryptedText.append(text.charAt(i));
                continue;
            }

            char charInText = text.charAt(i);
            char charInKey = key.charAt(indexOfKey);
            indexOfKey++;
            indexOfKey %= key.length();
            int sumOfChars = (charInText + charInKey) % 26;
            char encryptedChar = (char)(sumOfChars + 'A');

            encryptedText.append(encryptedChar);




        }

        return encryptedText.toString();
    }

    @Override
    public String decrypt(String text){
        int indexOfKey = 0;
        StringBuilder decryptedText = new StringBuilder();

        text = text.toUpperCase();
        text = textPocessor.removeAccent(text);

        for (int i = 0; i < text.length(); i++){

            if(text.charAt(i) < 'A' || text.charAt(i) > 'Z'){
                decryptedText.append(text.charAt(i));
                continue;
            }

            char charInText = text.charAt(i);
            char charInKey = key.charAt(indexOfKey);
            indexOfKey++;
            indexOfKey %= key.length();
            int sumOfChars = (charInText - charInKey + 26) % 26;
            char decryptedChar = (char)(sumOfChars + 'A');

            decryptedText.append(decryptedChar);
           
        }

        return decryptedText.toString();
    }

}
