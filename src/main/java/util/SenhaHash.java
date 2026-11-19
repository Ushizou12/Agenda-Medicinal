package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Gera e verifica hashes de senha com PBKDF2 (HmacSHA256) e salt aleatorio.
 *
 * <p>O texto guardado tem o formato {@code iteracoes$salt$hash} (salt e hash em
 * Base64). A senha original nao pode ser recuperada a partir dele; para conferir
 * uma senha digitada use {@link #verificar(String, String)}.</p>
 */
public final class SenhaHash {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACOES = 600_000;
    private static final int TAMANHO_SALT_BYTES = 16;
    private static final int TAMANHO_HASH_BITS = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private SenhaHash() {
    }

    /**
     * Gera o hash de uma senha. Cada chamada usa um salt novo, entao a mesma
     * senha produz textos diferentes.
     *
     * @param senha senha em texto puro
     * @return texto no formato {@code iteracoes$salt$hash}
     */
    public static String gerar(String senha) {
        byte[] salt = new byte[TAMANHO_SALT_BYTES];
        ALEATORIO.nextBytes(salt);
        byte[] hash = derivar(senha, salt, ITERACOES);

        Base64.Encoder base64 = Base64.getEncoder();
        return ITERACOES + "$" + base64.encodeToString(salt) + "$" + base64.encodeToString(hash);
    }

    /**
     * Confere se uma senha digitada corresponde ao hash guardado.
     *
     * @param senha senha em texto puro
     * @param armazenado texto gerado por {@link #gerar(String)}
     * @return {@code true} se a senha confere
     */
    public static boolean verificar(String senha, String armazenado) {
        if (senha == null || armazenado == null) {
            return false;
        }
        String[] partes = armazenado.split("\\$");
        if (partes.length != 3) {
            return false;
        }
        try {
            int iteracoes = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            byte[] calculado = derivar(senha, salt, iteracoes);
            return MessageDigest.isEqual(esperado, calculado); // comparacao em tempo constante
        } catch (IllegalArgumentException e) {
            return false; // formato invalido
        }
    }

    private static byte[] derivar(String senha, byte[] salt, int iteracoes) {
        PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, iteracoes, TAMANHO_HASH_BITS);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Nao foi possivel gerar o hash da senha.", e);
        } finally {
            spec.clearPassword();
        }
    }
}
