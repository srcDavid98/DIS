package util;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hashing {


    /**
     * Hashing that let's us scramble data to make it unreadable
     *
     * */

    public static String shaEncr(String rawString){

        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] hashing = messageDigest.digest(rawString.getBytes(StandardCharsets.UTF_8));

            String shaEncr = new String(HexBin.encode(hashing));

            return shaEncr;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } return rawString;
    }


    public static String hasWithSalt(String hash){
        String salt = "disexam";

        String HashedPassword = salt+hash;

        return shaEncr(HashedPassword);
    }
}


