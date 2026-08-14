public class MonoCipher implements Cipher{
    private final String key;

    public MonoCipher(String sKey){
        key = sKey;
    }


    private int getIndexOfChar(char character){
        return key.indexOf(character);
    }

    private char getChar(int index){
        return key.toCharArray()[index];
    }


    public String encrypt(String text){
        StringBuilder encryptedText = new StringBuilder();

        for (char character : text.toCharArray()){
            char encryptedChar = this.getChar(character - 'A');
            encryptedText.append(encryptedChar);
        }

        return encryptedText.toString();
    }
    
    public String decrypt(String text){
        StringBuilder decryptedText = new StringBuilder();

        for (char character : text.toCharArray()){
            char decryptedChar = (char)(this.getIndexOfChar(character) + 'A');
            decryptedText.append(decryptedChar);
        }

        return decryptedText.toString();
    }
    
}
