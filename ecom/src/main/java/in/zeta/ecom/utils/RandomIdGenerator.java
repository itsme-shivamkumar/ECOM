package in.zeta.ecom.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomIdGenerator {
    static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private final Random r = new Random();

    public String generateRandomId() {
        int length = 32 + (Math.abs(r.nextInt()) % 4);
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (Math.abs(r.nextInt()) % ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            res.append(randomChar);
        }
        return res.toString();
    }

    public String generateRandomId(int idLength) {
        int length = idLength + (Math.abs(r.nextInt()) % 4);
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (Math.abs(r.nextInt()) % ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            res.append(randomChar);
        }
        return res.toString();
    }

    public String generateRandomPhoneNumber() {
        String allowedCharactersNums = "0123456789";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int randomIndex = (Math.abs(r.nextInt()) % allowedCharactersNums.length());
            char randomChar = allowedCharactersNums.charAt(randomIndex);
            res.append(randomChar);
        }
        return "+91" + res;
    }

    public String generateRandomEmail() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomIndex = (Math.abs(r.nextInt()) % ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            res.append(randomChar);
        }
        return res + "@gmail.com";
    }
}
