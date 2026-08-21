package chat.cipher;

public class MonoCipher implements Cipher{
    private final String key;
    private ProcessText textPocessor = new ProcessText();

    public MonoCipher(String sKey){
        sKey = textPocessor.removeAccent(sKey.toUpperCase());
        textPocessor.checkMonoKey(sKey);
        key = sKey;
    }


    private int getIndexOfChar(char character){
        return key.indexOf(character);
    }

    private char getChar(int index){
        return key.charAt(index);
    }

    @Override
    public String encrypt(String text){
        text = text.toUpperCase();
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++){
            if(text.charAt(i) >= 'A' && text.charAt(i) <= 'Z'){
                char encryptedChar = this.getChar(text.charAt(i) - 'A');
                encryptedText.append(encryptedChar);


            }
            else{
                encryptedText.append(text.charAt(i));

            }

        }

        return encryptedText.toString();
    }
    

    public String decrypt(String text){
        text = text.toUpperCase();
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++){

            if(text.charAt(i) >= 'A' && text.charAt(i) <= 'Z'){
                char decryptedChar = (char)(this.getIndexOfChar(text.charAt(i)) + 'A');
                decryptedText.append(decryptedChar);

            }
            else{

                decryptedText.append(text.charAt(i));

            }


        }

        return decryptedText.toString();
    }
    
}
