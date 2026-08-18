public class VigenereCipher implements Cipher {
    private final String key;

    public VigenereCipher(String skey){
        key = skey;
    }

    @Override
    public String encrypt(String text){
        text = text.toUpperCase();
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++){
            if(text.charAt(i) >= 'A' && text.charAt(i) <= 'Z'){
                char charText = text.charAt(i);
                char charKey = key.charAt(i % key.length());
                int sumOfChars = (charText + charKey) % 26;

                char encryptedChar = (char)(sumOfChars + 'A');
                encryptedText.append(encryptedChar);

            }
            else{
                encryptedText.append(text.charAt(i));

            }


        }

        return encryptedText.toString();
    }

    @Override
    public String decrypt(String text){
        text = text.toUpperCase();
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++){

            if(text.charAt(i) >= 'A' && text.charAt(i) <= 'Z'){
                char charText = text.charAt(i);
                char charKey = key.charAt(i % key.length());
                int sumOfChars = (charText - charKey + 26) % 26;

                char decryptedChar = (char)(sumOfChars + 'A');
                decryptedText.append(decryptedChar);


            }
            else{
                decryptedText.append(text.charAt(i));

            }

        }

        return decryptedText.toString();
    }

}
