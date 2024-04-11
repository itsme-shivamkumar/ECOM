package in.zeta.ecom.utils;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

@Component
public class HashUtil {
    static byte[] salt = new byte[16];

    public static void main(String[] args) {
        salt = generateSalt();
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public String hashPassword(String password)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        return Arrays.toString(hash);
    }
}