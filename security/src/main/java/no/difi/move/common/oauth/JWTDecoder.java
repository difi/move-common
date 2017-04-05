package no.difi.move.common.oauth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.proc.BadJWSException;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

public class JWTDecoder {

    private KeystoreHelper keystoreHelper;
    private CertificateFactory certificateFactory;

    public JWTDecoder(KeystoreHelper keystoreHelper) throws CertificateException {
        this.certificateFactory = CertificateFactory.getInstance("X.509");
        this.keystoreHelper = keystoreHelper;
    }

    public String getPayload(String serialized) throws BadJWSException {

        JWSObject jwsObject;
        try {
            jwsObject = JWSObject.parse(serialized);
        } catch (ParseException e) {
            throw new BadJWSException("Could not parse signed string", e);
        }

        byte[] decode = jwsObject.getHeader().getX509CertChain().get(0).decode();
        Certificate certificate;
        try {
            certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(decode));
        } catch (CertificateException e) {
            throw new BadJWSException("Could not generate certificate object from JWS", e);
        }

        JWSVerifier jwsVerifier = new RSASSAVerifier((RSAPublicKey) certificate.getPublicKey());
        try {
            if (!jwsObject.verify(jwsVerifier)) {
                throw new BadJWSException("Signature did not successfully verify");
            }
        } catch (JOSEException e) {
            throw new BadJWSException("Could not verify JWS", e);
        }

        return jwsObject.getPayload().toString();
    }
}
