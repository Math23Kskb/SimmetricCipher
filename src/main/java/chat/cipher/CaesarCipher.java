public class CaesarCipher implements Cipher {
    private final int key;

    public CaesarCipher(int sKey){
        key = sKey;
    }


    @Override
    public String encrypt(String text){

        StringBuilder encryptedText = new StringBuilder();
        text = text.toUpperCase();
        for(int i = 0;i < text.length();i++){
            if(text.charAt(i) >= 'A' && text.charAt(i) <= 'Z'){
                char encryptedChar = text.charAt(i);
                encryptedChar -= 'A';
                int tmp = (encryptedChar + Math.floorMod(key,26)) % 26;
                encryptedChar = (char)(tmp + 'A');
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
        StringBuilder decryptedText = new StringBuilder();
        text = text.toUpperCase();
        for(int i = 0;i < text.length();i++){
            if(text.charAt(i) >= 'A' && text.charAt(i) <= 'Z'){
                char decryptedChar = text.charAt(i);
                decryptedChar -= 'A';
                int tmp = (decryptedChar + 26 - Math.floorMod(key,26)) % 26;
                decryptedChar = (char)(tmp + 'A');
                decryptedText.append(decryptedChar);

            }
            else{
                decryptedText.append(text.charAt(i));

            }


        }

        return decryptedText.toString();
    }
}
