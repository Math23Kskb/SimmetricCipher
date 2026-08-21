package chat.cipher;

import java.util.regex.Pattern;

public class ProcessText {
    public char convertLetter(char letter){
        switch (letter) {
            case 'Á':
                return 'A';
            case 'À':
                return 'A';
            case 'Â':
                return 'A';
            case 'Ã':
                return 'A';
            case 'É':
                return 'E';
            case 'È':
                return 'E';
            case 'Ê':
                return 'E';
            case 'Í':
                return 'I';
            case 'Ì':
                return 'I';
            case 'Ó':
                return 'O';
            case 'Ò':
                return 'O';
            case 'Ô':
                return 'O';
            case 'Õ':
                return 'O';
            case 'Ú':
                return 'U';
            case 'Ù':
                return 'U';
            case 'Ü':
                return 'U';
            case 'Ç':
                return 'C';
            default:
                return letter;
            
        }


    }

    public String removeNonLetters(String string){
        StringBuilder newString = new StringBuilder();
        for(int i = 0; i < string.length(); i++){
            if(string.charAt(i) < 'A' || string.charAt(i) > 'Z'){
                continue;
            }
            newString.append(string.charAt(i));

        }

        return newString.toString();
    }

    public String removeAccent(String string){
        StringBuilder newString = new StringBuilder();

        for(int i = 0; i < string.length(); i++){
            newString.append(convertLetter(string.charAt(i)));
        }

        return newString.toString();
    }

    public void checkMonoKey(String key) throws IllegalArgumentException{
        if(key.length() != 26){
            throw new IllegalArgumentException("The key must have exactly 26 characters");

        }

        char letter;
        boolean isEqual;
        for(int i = 0; i < 26; i++){
            isEqual = false;
            letter = (char)(i + 'A');
            for(int j = 0; j < key.length(); j++){
                if(letter == key.charAt(j)){
                    isEqual = true;
                }

            }
            if(!isEqual){
                throw new IllegalArgumentException("Missing letters in the key!");
            }
        }

    }

    public void checkVigenere(String key) throws IllegalArgumentException{
        for(int i = 0; i < key.length(); i++){
            if(key.charAt(i) < 'A' || key.charAt(i) > 'Z'){
                throw new IllegalArgumentException("No special characters allowed!");

            }
        }

    }

    public void checkPlayfair(String key) throws IllegalArgumentException{
        for(int i = 0; i < key.length(); i++){
            if(key.charAt(i) == ' '){
                continue;
            }
            if(key.charAt(i) < 'A' || key.charAt(i) > 'Z'){
                throw new IllegalArgumentException("No special characters allowed!");

            }
        }

    }



    public String removeDuplicates(String text){
        boolean[] isEqual = new boolean[text.length()];
        StringBuilder newText = new StringBuilder();
        for(int i = 0; i < text.length() - 1; i++){
            for(int j = i + 1; j < text.length(); j++){
                if(text.charAt(i) == text.charAt(j)){
                    isEqual[j] = true;
                }

            }
            if(!isEqual[i]){
                newText.append(text.charAt(i));
            }
        }

        return newText.toString();
    }

    public String turnJIntoI(String string){
        StringBuilder newString = new StringBuilder();
        for(int i = 0; i < string.length(); i++){
            if(string.charAt(i) == 'J'){
                newString.append('I');
            }
            else{
                newString.append(string.charAt(i));
            }
        }

        return newString.toString();
    }

    public String removeLetters(String text1, String text2) {
        if (text1 == null || text2 == null || text2.isEmpty()) {
            return text1;
        }

        String pattern = "[" + Pattern.quote(text2) + "]";
        return text1.replaceAll(pattern, "");
    }

    
}
