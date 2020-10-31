package server.util;



    public class Encryption {

        /**
         * Encrypt method that allows to encrypt/decrypt data
         *
         * */
        public static String encryptDecrypt(String rawString) {

            String key2 = "remixultra";
            //Chars som brukes til å kryptere informasjonen som sendes
            char[] key = key2.toCharArray();


            StringBuilder thisIsEncrypted = new StringBuilder();


            for (int i = 0; i < rawString.length(); i++) {
                thisIsEncrypted.append((char) (rawString.charAt(i) ^ key[i % key.length]));
            }

            // We return the encrypted string
            return thisIsEncrypted.toString();
        }
    }

