package basetest.java24;

import javax.crypto.KEM;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.NamedParameterSpec;

public class LanguageFeatureTest {

    public static  void main(String[] args) throws Exception {
        qrMlDsaTest();
    }

    // Key Derivation Function API - Cryptography algorithm to derive keys from passwords and salts
    static void keyDerivationTest() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec("password".toCharArray(), "salt".getBytes(), 10, 256);
        SecretKey key = factory.generateSecret(spec);
    }

    record PublicPrivateKeyPair(PublicKey publicKey, PrivateKey privateKey) {}
    static PublicPrivateKeyPair generateKeys() throws Exception {
        // Initialize the KeyPairGenerator with the ML-KEM algorithm
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ML-KEM");
        // Specify the parameter set; ML-KEM-512 is used here
        keyPairGenerator.initialize(new NamedParameterSpec("ML-KEM-512"));
        // Generate the key pair
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        System.out.println("ML-KEM Key Pair generated successfully.");
        return new PublicPrivateKeyPair(keyPair.getPublic(), keyPair.getPrivate());
    }

    static void qrMlKemUsageTest() throws Exception {
        // Generate a public/private key pair
        var keys = generateKeys();
        // Encapsulate a symmetric key using the public key
        KEM kem = KEM.getInstance("ML-KEM");
        KEM.Encapsulator encapsulator = kem.newEncapsulator(keys.publicKey);
        // Encapsulate the secret key
        KEM.Encapsulated encapsulated = encapsulator.encapsulate();
        // Retrieve the encapsulated message to send to the recipient
        byte[] encapsulation = encapsulated.encapsulation();
        // The sender's copy of the secret key
        SecretKey secretKeySender = encapsulated.key();


        // Assuming 'privateKey' is the recipient's ML-KEM private key
        KEM.Decapsulator decapsulator = kem.newDecapsulator(keys.privateKey);
        // Decapsulate the secret key using the received encapsulation
        SecretKey secretKeyReceiver = decapsulator.decapsulate(encapsulation);
    }

    static void qrMlDsaTest() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ML-DSA");
        KeyPair kp = kpg.generateKeyPair();
        var publicKey = kp.getPublic();
        var privateKey = kp.getPrivate();

        var data = "Important message".getBytes();

        // Sign data
        Signature sig = Signature.getInstance("ML-DSA");
        sig.initSign(privateKey);
        sig.update(data);
        byte[] signature = sig.sign();

        // Verify signature
        sig.initVerify(publicKey);
        sig.update(data);
        boolean isVerified = sig.verify(signature);
        System.out.println("Signature verified: " + isVerified);
    }

}
