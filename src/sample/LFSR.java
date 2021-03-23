package sample;

class LFSR {

    static StringBuilder encryptDecryptFileBits(StringBuilder key, String inpBinaryFileBits) {
        StringBuilder cipherBits = new StringBuilder();

        for (int i = 0; i < inpBinaryFileBits.length(); i++) {
            boolean inpBinFileBit = inpBinaryFileBits.charAt(i) == '1';
            boolean keyBit = key.charAt(i) == '1';
            boolean resBit = inpBinFileBit ^ keyBit;
            cipherBits.append(resBit ? '1' : '0');
        }
        return cipherBits;
    }


    static StringBuilder generateKeyBits(String keyState, String inpBinaryFile) {
        StringBuilder keyBits = new StringBuilder();
        boolean[] stateOfRegister = initStateOfRegister(keyState);
        int lenOfRegister = stateOfRegister.length;
        int regBit = 1;

        for (int t = 0; t < inpBinaryFile.length(); t++) {

            boolean zeroBit = (stateOfRegister[lenOfRegister - 1] ^ stateOfRegister[regBit]);
            boolean nextKeyBit = stateOfRegister[lenOfRegister - 1];
            keyBits.append(nextKeyBit ? '1' : '0');

            for (int i = lenOfRegister - 1; i > 0; i--) {
                stateOfRegister[i] = stateOfRegister[i - 1];
            }

            stateOfRegister[0] = zeroBit;
        }
        return keyBits;
    }

    private static boolean[] initStateOfRegister(String keyState) {
        boolean[] stateOfRegister = new boolean[keyState.length()];
        for (int c = 0, i = stateOfRegister.length - 1; i > -1; i--) {
            stateOfRegister[i] = keyState.charAt(c++) == '1';
        }
        return stateOfRegister;
    }
}
