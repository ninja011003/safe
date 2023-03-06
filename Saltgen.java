import java.security.SecureRandom;
import java.util.Base64;

public class Saltgen {
    public static byte[] generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return salt;
    }
    public static byte[] salt ;
    public static void save(){
        salt= Base64.getEncoder().encodeToString(generateSalt(16)).getBytes();
    }
    public static void main(String[] args) {
        
    }
}
