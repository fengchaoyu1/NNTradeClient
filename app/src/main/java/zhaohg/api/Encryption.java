package zhaohg.api;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {

    public static String md5(String text) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encoded = messageDigest.digest(text.getBytes());
        BigInteger bigInt = new BigInteger(1, encoded);
        String hashText = bigInt.toString(16);
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return hashText;
    }

}
