package basetest.java25;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LanguageFeatureTest {


    // Demonstrates primitive type support in pattern matching
    static void primitiveTypeSupportInPatterns() {
        Object obj = 42; // This can be any object
        if (obj instanceof int i) { // Pattern matching for primitive type
            System.out.println("The integer value is: " + i);
        } else {
            System.out.println("The object is not an integer.");
        }
    }

    // Demonstrates scoped values with virtual threads
    static final ScopedValue<String> USER = ScopedValue.newInstance();
    static void scopedValues() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> ScopedValue.where(USER, "Alice").run(() -> {
                System.out.println("Thread: " + Thread.currentThread());
                System.out.println("User: " + USER.get());
            }));

            executor.submit(() -> ScopedValue.where(USER, "Bob").run(() -> {
                System.out.println("Thread: " + Thread.currentThread());
                System.out.println("User: " + USER.get());
            }));

            // Optional delay to ensure output appears before main exits
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Demonstrates stable values - immutable values across threads, extension of Optional
    static void stableValueExample() {
        var stableValue = StableValue.of(100);
        System.out.println("Stable Value: " + stableValue.orElse(-1));
    }

    // Demonstrates PEM encoding of cryptographic objects
    static void pemEncodingOfCryptographicObjects() {
        String pem = """
        -----BEGIN PUBLIC KEY-----
        MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgjDohS0RHP395oJxciVaeks9N
        KNY5m9V1IkBBwYsMGyxskrW5sapgi9qlGSYOma9kkko1xlBs17qG8TTg38faxgGJ
        sLT2BAmdVFwuWdRtzq6ONn2YPHYj5s5pqx6vU5baz58/STQXNIhn21QoPjXgQCnj
        Pp0OxnacWeRSnAIOmQIDAQAB
        -----END PUBLIC KEY-----
        """;

        try {
            String base64 = pem.replaceAll("-----.*-----", "").replaceAll("\\s", "");
            byte[] keyBytes = Base64.getDecoder().decode(base64);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey key = factory.generatePublic(spec);

            System.out.println("Loaded key: " + key.getAlgorithm());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    // Demonstrates password-based key derivation
    static void passwordBasedKeyDerivation() throws Exception {
        char[] password = "hunter2".toCharArray();
        byte[] salt = "somesalt".getBytes();
        PBEKeySpec spec = new PBEKeySpec(password, salt, 65536, 256);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        SecretKey key = factory.generateSecret(spec);

        System.out.println("Derived key format: " + key.getFormat());
    }



}
