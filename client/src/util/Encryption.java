package util;

public class Encryption {

    public static String encryptDecrypt(String rawString) {



        String key2 = "remixultra";
        char[] key = key2.toCharArray();


        /**
         *Encryption is slightly bugged because of StringBuilder!
         * We use double conversion to avoid the bug! --> Tip was given by Morten
         * */

        StringBuilder thisIsEncrypted = new StringBuilder();

        for (int i = 0; i < rawString.length(); i++) {
            thisIsEncrypted.append((char) (rawString.charAt(i) ^ key[i % key.length]));
        }

        // We return the encrypted string
        return thisIsEncrypted.toString();
    }
}