package org.example.demo1.common;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

public class JWTUtil {
    public static final String SECRET;

    static {
        String envSecret = System.getenv("JWT_SECRET_KEY");
        if (envSecret == null || envSecret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET_KEY environment variable must be set and have 32 characters.");
        }
        SECRET = envSecret;
    }

    private static final long  EXPIRATION_TIME = 1000 * 60 * 15; // 15 minutes expiration time

    public static String generateToken(String email, String role){
        try{
            JWSSigner signer = new MACSigner(SECRET);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .claim("role",role)
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .issueTime(new Date())
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.HS256)
                            .type(JOSEObjectType.JWT)
                            .build(),
                    claimsSet
            );
            signedJWT.sign(signer);
            return signedJWT.serialize();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isTokenExpired(SignedJWT jwt){
        try{
            Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
            return expirationTime.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean validateToken(String token){
        try{
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET);
            return jwt.verify(verifier) && !isTokenExpired(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JWTClaimsSet getJWTClaimsSet(String token){
        try{
            SignedJWT jwt = SignedJWT.parse(token);
            return jwt.getJWTClaimsSet();
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractRoleFromHeader(String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return null;
        }
        String token = authHeader.substring("Bearer ".length()).trim();

        if(!validateToken(token)){
            return null;
        }

        try{
            JWTClaimsSet claimsSet = getJWTClaimsSet(token);
            return claimsSet.getStringClaim("role");
        } catch (Exception e){
            return null;
        }
    }
}
