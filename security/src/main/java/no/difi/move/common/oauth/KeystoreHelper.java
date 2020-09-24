package no.difi.move.common.oauth;

import lombok.extern.slf4j.Slf4j;
import no.difi.asic.SignatureHelper;
import no.difi.move.common.config.KeystoreProperties;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * Class responsible for accessing the keystore for the Integrasjonspunkt.
 *
 * @author Glebnn Bech
 */
@Slf4j
public class KeystoreHelper {

    private final KeystoreProperties keystore;

    public KeystoreHelper(KeystoreProperties keystore) {
        this.keystore = keystore;
    }

    /**
     * Loads the private key from the keystore
     *
     * @return the private key
     */
    public PrivateKey loadPrivateKey() {

        PrivateKey key = null;
        try (InputStream i = this.keystore.getLocation().getInputStream()) {
            KeyStore keystore = getKeystore(i);
            if (!isKeyEntry(keystore)) {
                throw new RuntimeException("no private key with alias " + this.keystore.getAlias() + " found in the keystore "
                        + this.keystore.getLocation());
            }
            key = (PrivateKey) keystore.getKey(this.keystore.getAlias(), this.keystore.getEntryPassword().toCharArray());
            return key;
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyPair getKeyPair() {

        KeyPair result = null;
        try (InputStream i = this.keystore.getLocation().getInputStream()) {
            KeyStore keystore = getKeystore(i);
            if (!isKeyEntry(keystore)) {
                throw new RuntimeException("no key pair with alias " + this.keystore.getAlias() + " found in the keystore "
                        + this.keystore.getLocation());
            }
            PrivateKey key = (PrivateKey) keystore.getKey(this.keystore.getAlias(), this.keystore.getEntryPassword().toCharArray());
            X509Certificate c = (X509Certificate) keystore.getCertificate(this.keystore.getAlias());
            result = new KeyPair(c.getPublicKey(), key);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public X509Certificate getX509Certificate() {

        X509Certificate result = null;
        try (InputStream i = this.keystore.getLocation().getInputStream()) {
            KeyStore keystore = getKeystore(i);
            if (!isKeyEntry(keystore)) {
                throw new RuntimeException("no certificate with alias " + this.keystore.getAlias() + " found in the keystore "
                        + this.keystore.getLocation());
            }
            result = (X509Certificate) keystore.getCertificate(this.keystore.getAlias());
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public SignatureHelper getSignatureHelper() {
        try {
            InputStream keyInputStream = this.keystore.getLocation().getInputStream();
            return new SignatureHelper(keyInputStream, this.keystore.getStorePassword(), this.keystore.getAlias(), this.keystore.getEntryPassword());
        } catch (IOException e) {
            throw new RuntimeException("keystore " + this.keystore.getLocation() + " not found on file system.");
        }
    }

    private boolean isKeyEntry(final KeyStore keystore) throws IOException, RuntimeException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
        Enumeration aliases = keystore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = (String) aliases.nextElement();
            log.debug("Found: " + alias + " in " + this.keystore.getLocation());
            boolean isKey = keystore.isKeyEntry(alias);
            if (isKey && alias.equals(this.keystore.getAlias())) {
                return true;
            }
        }
        return false;
    }

    private KeyStore getKeystore(final InputStream i) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        KeyStore keystore = KeyStore.getInstance(this.keystore.getType());
        keystore.load(i, this.keystore.getStorePassword().toCharArray());
        return keystore;
    }

}
