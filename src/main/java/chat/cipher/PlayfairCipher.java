import java.util.regex.Pattern;

public class PlayfairCipher implements Cipher {
    private final String key;
    private final char[][] matrix = new char[5][5];

    public PlayfairCipher(String sKey){
        key = sKey;
        makeMatrix();
    }

    private String makeAlphabet(){
        StringBuilder alphabetString = new StringBuilder();
        for(int i = 0; i < 26; i++){
            if(i == 9){
                continue;
            }
            alphabetString.append((char)(i + 'A'));
        }

        return alphabetString.toString();
    }

    private String removeDuplicates(String text){
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

    private String removeLetters(String text1, String text2) {
        if (text1 == null || text2 == null || text2.isEmpty()) {
            return text1;
        }

        String pattern = "[" + Pattern.quote(text2) + "]";
        return text1.replaceAll(pattern, "");
    }

    private String turnJIntoI(String text){
        StringBuilder newText = new StringBuilder();
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == 'J'){
                newText.append('I');
            }
            else{
                newText.append(text.charAt(i));
            }
        }

        return newText.toString();
    }

    private void makeMatrix(){
        String newKey = turnJIntoI(key);
        newKey = removeDuplicates(newKey);
        String finalText = newKey + removeLetters(makeAlphabet(), newKey);
        char[] finalTextArray = finalText.toCharArray();
        for(int i = 0; i < finalTextArray.length; i++){
            matrix[i/5][i % 5] = finalTextArray[i];
        }

    }


    private String makeDigraphs(String text){
        for(int i = 0; i < text.length(); i++){
            if(i == text.length() - 1){
                continue;
            }

            if(text.charAt(i) == text.charAt(i + 1)){
                text = text.substring(0, i + 1) + "X" + text.substring(i + 1);
            }
        }

        if(text.length() % 2 > 0){
            text += "X";
        }
        
        return text;
    }

    private int[] getIndexAtMatrix(char character){
        int[] indexAtMatrix = new int[2];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(matrix[i][j] == character){
                    indexAtMatrix[0] = i;
                    indexAtMatrix[1] = j;
                    return indexAtMatrix;
                }
            }
        }
        indexAtMatrix[0] = -1;
        indexAtMatrix[1] = -1;
        return indexAtMatrix; 
    }


    private char getCharAtMatrix(int line, int column){

        return matrix[line][column];

    }

    private String processText(String text){
        StringBuilder newText = new StringBuilder();
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) < 'A' || text.charAt(i) > 'Z'){
                continue;
            }
            newText.append(text.charAt(i));

        }

        return newText.toString();
    }

    private String matrixString(){
        StringBuilder toStringMatrix = new StringBuilder();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                toStringMatrix.append(matrix[i][j]);
                toStringMatrix.append(' ');

            }

            toStringMatrix.append("\n");
        }

        return toStringMatrix.toString();

    }

    private String removeX(String text){
        StringBuilder newText = new StringBuilder();

        for(int i = 0; i < text.length(); i++){


            if((text.charAt(i) == 'X') && (i < text.length() - 1) && (text.charAt(i - 1) == text.charAt(i + 1))){
                continue;

            }
            newText.append(text.charAt(i));

        }
        return newText.toString();

    }
    //--------------Métodos principais----------------

    @Override
    public String encrypt(String text){

        text = text.toUpperCase();
        text = processText(text);
        text = turnJIntoI(text);
        text = makeDigraphs(text);

        StringBuilder encryptedText = new StringBuilder();

        for(int i = 0; i < text.length() - 1; i+=2){
            char encryptedChar0;
            char encryptedChar1;

            int[] index0 = getIndexAtMatrix(text.charAt(i));
            int[] index1 = getIndexAtMatrix(text.charAt(i+1));

            if(index0[0] == -1 || index0[1] == -1 || index1[0] == -1 || index1[1] == -1){
                encryptedChar0 = '0';
                encryptedChar1 = '1';

            }

            else if(index0[0] == index1[0]){
                 encryptedChar0 = getCharAtMatrix(index0[0], (index1[1] + 1) % 5);
                 encryptedChar1 = getCharAtMatrix(index1[0], (index0[1] + 1) % 5);


            }
            else if(index0[0] == index1[0]){
                 encryptedChar0 = getCharAtMatrix((index0[0] + 1) % 5, index1[1]);
                 encryptedChar1 = getCharAtMatrix((index1[0] + 1) % 5, index0[1]);

            }
            else{
                 encryptedChar0 = getCharAtMatrix(index0[0], index1[1]);
                 encryptedChar1 = getCharAtMatrix(index1[0], index0[1]);

            }

            String digraph = String.valueOf(encryptedChar0) + String.valueOf(encryptedChar1);
            encryptedText.append(digraph);

        }

        return encryptedText.toString();


    }

    @Override
    public String decrypt(String text){
        text = text.toUpperCase();
        text = turnJIntoI(text);
        text = processText(text);
        text = makeDigraphs(text);

        StringBuilder decryptedText = new StringBuilder();

        for(int i = 0; i < text.length() - 1; i+=2){
            char decryptedChar0;
            char decryptedChar1;

            int[] index0 = getIndexAtMatrix(text.charAt(i));
            int[] index1 = getIndexAtMatrix(text.charAt(i+1));

            if(index0[0] == -1 || index0[1] == -1 || index1[0] == -1 || index1[1] == -1){
                decryptedChar0 = '0';
                decryptedChar1 = '1';

            }

            else if(index0[0] == index1[0]){
                 decryptedChar0 = getCharAtMatrix(index0[0], (index1[1] + 4) % 5);
                 decryptedChar1 = getCharAtMatrix(index1[0], (index0[1] + 4) % 5);


            }
            else if(index0[0] == index1[0]){
                 decryptedChar0 = getCharAtMatrix((index0[0] + 4) % 5, index1[1]);
                 decryptedChar1 = getCharAtMatrix((index1[0] + 4) % 5, index0[1]);

            }
            else{
                 decryptedChar0 = getCharAtMatrix(index0[0], index1[1]);
                 decryptedChar1 = getCharAtMatrix(index1[0], index0[1]);

            }

            String digraph = String.valueOf(decryptedChar0) + String.valueOf(decryptedChar1);
            decryptedText.append(digraph);

        }



        return decryptedText.toString();


    }

}
