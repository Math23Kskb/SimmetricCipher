import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class DESCipher{
    private final long key;
    private final long[] subkeys;



    // **************TABELAS****************
    private static final byte[] IP = {
    58, 50, 42, 34, 26, 18, 10, 2,
    60, 52, 44, 36, 28, 20, 12, 4,
    62, 54, 46, 38, 30, 22, 14, 6,
    64, 56, 48, 40, 32, 24, 16, 8,
    57, 49, 41, 33, 25, 17, 9,  1,
    59, 51, 43, 35, 27, 19, 11, 3,
    61, 53, 45, 37, 29, 21, 13, 5,
    63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final byte[] E = {
    32, 1, 2, 3, 4, 5, 4, 5,
    6, 7, 8, 9, 8, 9, 10, 11,
    12, 13, 12, 13, 14, 15, 16, 17,
    16, 17, 18, 19, 20, 21, 20, 21,
    22, 23, 24, 25, 24, 25, 26, 27,
    28, 29, 28, 29, 30, 31, 32, 1
    };

    private static final byte[] PC1 = {
    57, 49, 41, 33, 25, 17, 9,
    1, 58, 50, 42, 34, 26, 18,
    10, 2, 59, 51, 43, 35, 27,
    19, 11, 3, 60, 52, 44, 36,
    63, 55, 47, 39, 31, 23, 15,
    7, 62, 54, 46, 38, 30, 22,
    14, 6, 61, 53, 45, 37, 29,
    21, 13, 5, 28, 20, 12, 4
    };

    private static final byte[] PC2 = {
    14, 17, 11, 24, 1, 5,
    3, 28, 15, 6, 21, 10,
    23, 19, 12, 4, 26, 8,
    16, 7, 27, 20, 13, 2,
    41, 52, 31, 37, 47, 55,
    30, 40, 51, 45, 33, 48,
    44, 49, 39, 56, 34, 53,
    46, 42, 50, 36, 29, 32
    };

    private static final byte[] SHIFTS = {
    1, 1, 2, 2,
    2, 2, 2, 2,
    1, 2, 2, 2,
    2, 2, 2, 1
    };

    private static final byte[] SP = {
    16, 7, 20, 21,
    29, 12, 28, 17,
    1, 15, 23, 26,
    5, 18, 31, 10,
    2, 8, 24, 14,
    32, 27, 3, 9,
    19, 13, 30, 6,
    22, 11, 4, 25
    };

    private final static byte[][][] SBOX =
    {{
        { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
        { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
        { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
        { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
    },
    {
        { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
        { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
        { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
        { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
    },
    {
        { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
        { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
        { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
        { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
    },
    {
        { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
        { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
        { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
        { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
    },
    {
        { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
        { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
        { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
        { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
    },
    {
        { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
        { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
        { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
        { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
    },
    {
        { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
        { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
        { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
        { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
    },
    {
        { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
        { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
        { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
        { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    }};

    private final static byte[] FP =
    {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25

    };
    //**************************************************************



    public DESCipher(String sKey){
        key = Long.parseLong(sKey,16);

        long newKey = PC1Permutation(key);
        subkeys = generateSubkeys(newKey);


    }


    public long PC1Permutation(long longKey) {

        long result = 0;

        for (int i = 0; i < PC1.length; i++) {

            int pos = PC1[i];


            long bit = (longKey >> (64 - pos)) & 1L;


            result |= bit << (55 - i);
        }

        return result;
    }

    public long[] generateSubkeys(long key56) {

        long[] keys = new long[16];

        long C = (key56 >> 28) & 0x0FFFFFFFL;
        long D = key56 & 0x0FFFFFFFL;

        for (int i = 0; i < 16; i++) {


            C = leftShift28(C, SHIFTS[i]);
            D = leftShift28(D, SHIFTS[i]);


            long combined = (C << 28) | D;


            keys[i] = PC2Permutation(combined);
        }

        return keys;
    }

    public long leftShift28(long value, int shifts) {

        value &= 0x0FFFFFFFL;

        return ((value << shifts) |
                (value >> (28 - shifts))) & 0x0FFFFFFFL;
    }


    public long PC2Permutation(long longKey) {

        long result = 0;

        for (int i = 0; i < PC2.length; i++) {

            int pos = PC2[i];

            long bit = (longKey >> (56 - pos)) & 1L;

            result |= bit << (47 - i);
        }

        return result;
    }

    public long IPPermutation(long longText) {

        long result = 0;

        for (int i = 0; i < IP.length; i++) {

            int pos = IP[i];

            long bit = (longText >> (64 - pos)) & 1L;

            result |= bit << (63 - i);
        }

        return result;
    }


    public long FinalPermutation(long block) {

        long result = 0;

        for (int i = 0; i < FP.length; i++) {

            int pos = FP[i];

            long bit = (block >>> (64 - pos)) & 1L;

            result |= bit << (63 - i);
        }

        return result;
}

    public long expansion(long R) {

        long result = 0;

        for (int i = 0; i < E.length; i++) {

            int pos = E[i];

            long bit = (R >>> (32 - pos)) & 1L;

            result |= bit << (47 - i);
        }

        return result;
    }

    public long SPermutation(long block) {

        long result = 0;

        for (int i = 0; i < SP.length; i++) {

            int pos = SP[i];

            long bit = (block >>> (32 - pos)) & 1L;

            result |= bit << (31 - i);
        }

        return result;
    }

    public int SBox(int block, byte[][] S) {

        int row = ((block & 0b100000) >>> 4)
                | (block & 0b000001);

        int col = (block >>> 1) & 0b1111;

        return S[row][col];
    }

    public long sBoxes(long entry) {

        long result = 0;

        for (int i = 0; i < 8; i++) {

            int block = (int)((entry >>> (42 - 6 * i)) & 0x3F);

            int value = SBox(block, SBOX[i]);

            result = (result << 4) | value;
        }

        return result;
    }



    public long feistel(long R, long K){

        long textExpanded = expansion(R);


        long result = textExpanded ^ K;


        result = sBoxes(result);


        result = SPermutation(result);

    return result;

    }

   public long[] round(long L, long R, long K) {

        long newL = R;

        long newR = L ^ feistel(R, K);

        return new long[] { newL, newR };
    }


    public long DESEncrypt(long longText){
        longText = IPPermutation(longText);

        long L = (longText >>> 32) & 0xFFFFFFFFL;
        long R = longText & 0xFFFFFFFFL;


        for(int i = 0; i < 16; i++){
            long[] vec = round(L,R,subkeys[i]);
            L = vec[0];
            R = vec[1];


        }

        long joinedHalves = (R << 32 | L);

        return FinalPermutation(joinedHalves);

    }

    public long DESDecrypt(long cipherLong){
        long permutado = IPPermutation(cipherLong);


        long L = (permutado >>> 32) & 0xFFFFFFFFL;
        long R = permutado & 0xFFFFFFFFL;


        for (int i = 15; i >= 0; i--) {

            long[] vec = round(L, R, subkeys[i]);

            L = vec[0];
            R = vec[1];
        }


        long blocoFinal = (R << 32) | (L & 0xFFFFFFFFL);


        return FinalPermutation(blocoFinal);

    }

    public String encrypt(String text) {

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);

        int resto = bytes.length % 8;
        int padding = (8 - resto) % 8;

        byte[] padded = new byte[bytes.length + padding];

        System.arraycopy(bytes, 0, padded, 0, bytes.length);

        long[] blocos = new long[padded.length / 8];


        for (int i = 0; i < padded.length; i += 8) {

            long bloco = 0;

            for (int j = 0; j < 8; j++) {
                bloco = (bloco << 8) | (padded[i + j] & 0xFFL);
            }

            blocos[i / 8] = bloco;
        }


        StringBuilder a = new StringBuilder();

        for (long item : blocos) {

            long cifrado = DESEncrypt(item);

            a.append(String.format("%016X", cifrado));
            a.append(" ");
        }

        return a.toString();
    }

    public String decrypt(String encryptedText) {

        String[] blocos = encryptedText.trim().split("\\s+");

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (String blocoHex : blocos) {

            long bloco = Long.parseUnsignedLong(blocoHex, 16);

            long decifrado = DESDecrypt(bloco);

            for (int i = 7; i >= 0; i--) {
                int shift = i * 8;
                output.write((int) ((decifrado >>> shift) & 0xFF));
            }
        }

        byte[] dados = output.toByteArray();


        int tamanho = dados.length;

        while (tamanho > 0 && dados[tamanho - 1] == 0) {
            tamanho--;
        }

        return new String(dados, 0, tamanho, StandardCharsets.UTF_8);
    }

}
