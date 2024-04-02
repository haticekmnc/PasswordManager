import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String CHARSET_NAME = "UTF-8";
    private static byte[] KEY;
    private static byte[] IV;

    static {
    try {
        // 128 bit (16 bayt) uzunluğunda bir anahtar örneği
        KEY = "1234567891111111".getBytes(CHARSET_NAME);
        IV = "yourRandom1111111".getBytes(CHARSET_NAME); // IV'nin uzunluğu 16 bayt olmalıdır.
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        KEY = "1234567891111111".getBytes();
        IV = "yourRandomIV1111111".getBytes();
    }
}


    public static String encrypt(String valueToEnc) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(valueToEnc.getBytes(CHARSET_NAME));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedValue) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
        return new String(original, CHARSET_NAME);
    }
}
