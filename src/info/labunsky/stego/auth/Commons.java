package info.labunsky.stego.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Commons {
    static String ALPHABET_REGEX = "[^0-9a-zа-я ]";

    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    static byte[] hash(String message) {
        for (String token : message.toLowerCase().replaceAll(ALPHABET_REGEX, "").split(" "))
            md.update(token.getBytes());
        return md.digest();
    }
}
