package app.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

    /**
     * This class provides helper methods to:
     * Encode secure user password into Base64;
     * Generate Salt value;
     * Use password-based encryption to encrypt user password;
     * Verify entered password.
     * {@link}          https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
     */
public class PasswordUtils {

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    /**
     * This method generates the salt.
     * The generated value can be stored in the database.
     * {@link}              https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
     * @param length        Accepts an integer as the length of Salt to be generated.
     * @return String       Returns string with the generated salt.
     */
    public static String getSalt(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    /**
     * This method generates the salt.
     * The generated value can be stored in the database.
     * {@link}              https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
     * @param password      User entered password value.
     * @param salt          Generated salt value.
     * @return byte[]       Returns byte array.
     */
    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * This method generates the secure password.
     * {@link}              https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
     * @param password      User entered password value.
     * @param salt          Generated salt value.
     * @return String       Returns the secure password string.
     */
    public static String generateSecurePassword(String password, String salt) {
        String returnValue = null;

        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }

    /**
     * This method verifies if the entered user password is the same as the one stored in the database.
     * {@link}                  https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
     * @param providedPassword  User entered password value.
     * @param securedPassword   Previously generated and saved password on the database.
     * @param salt              Previously generated and saved salt value.
     * @return boolean          Returns true or false.
     */
    public static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) {
        boolean returnValue = false;

        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);

        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }

//    String myPassword = "1";
//
//    // Generate Salt. The generated value can be stored in DB.
//    String salt = PasswordUtils.getSalt(30);
//
//    // Protect user's password. The generated value can be stored in DB.
//    String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);
//
//    // Print out protected password
//        System.out.println("My secure password = " + mySecurePassword);
//        System.out.println("Salt value = " + salt);
}
