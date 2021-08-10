package backend.authentication.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashAlgorithmUtil {

  public static String getSHA256Hash(final String source) throws NoSuchAlgorithmException {
    if (source == null) {
      return null;
    }
    final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    return String.format("%064x", new BigInteger(1, messageDigest.digest(source.getBytes(StandardCharsets.UTF_8))));
  }

  public static boolean validateSHA256Hash(final String source, final String targetHash) throws NoSuchAlgorithmException {
    final String sourceHash = getSHA256Hash(source);
    if (sourceHash == null || targetHash == null) {
      return false;
    }
    return sourceHash.equals(targetHash);
  }
}