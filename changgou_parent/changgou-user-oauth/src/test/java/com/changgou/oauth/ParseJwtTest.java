package com.changgou.oauth;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {
    @Test
    public void parseJwt() {
        //基于公钥解析iwt
        //String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZGRyZXNzIjoiYmVpamluZyIsImNvbXBhbnkiOiJoZWltYSJ9.cjZNz8G0m4noNYN2VM1SH3ujAtbHElW5Vtbadb0NDI0cjM1DaAXzMA53Qbj4pmVQPl_IfSKqUEXbLxowdRa5NHR43laFsR0kzGbJiTINfSVSroSslYpDdEVwCeAF_a7I-R819YTj4p6sjuYKXbzXpeZQErczFbWWWGR2_U44xH6u1ejRNv8PikFiuzNw-muL7zUJkvqeSJzbEMnQdZMbfvZp4LtSI6B4G_PqpdNXkv19-juxAh99VgJInH_ItF0y5IBOxofA7gRebCZmU8L57gO9ohf2L00D95kis_Ji8lmA1ptLIfXqO_qLVvLBUNH-VtgjGAF0-0pyB-5jlbHP7w";
        String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3MzI0OTYxMywiYXV0aG9yaXRpZXMiOlsiYWNjb3VudGFudCIsInVzZXIiLCJzYWxlc21hbiJdLCJqdGkiOiJmYzQzN2UwZC0wMTkxLTQ0NTAtYTM2Zi1lNjIzMTI1N2FiMTkiLCJjbGllbnRfaWQiOiJjaGFuZ2dvdSIsInVzZXJuYW1lIjoiaGVpbWEifQ.bwpcAa-soiUXxBHtqkSTyvHD-cF-D6JQuoTKHFIC_o3AUdminnbfhqkqG6YfCDNkWjs0cvL9UoYjLL0OoCJBxNnQPSvebHNyu0Nj9E0R3W9OFZcWUqDW504Wb67uB4Dl-ifWZPvq2Dm8s6r_URZmdrUPExcRnMtfuPdl6h2ZES3DU0mSLx9Vowc2pVqnTkTNYBllI1VhHPZu4xtkt5HtUwIGbTLlP25OAh_dz757a1tr91mjtjyHNPUsh154JsD26KU___VTTaKtJEMavGPfszM70wP6C2mTYwOfJ9RIYPcEeZiXZHO8sR9BoiQx9wEMJoKfrWZMLhc5hw-YPGmbyw";

        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvFsEiaLvij9C1Mz+oyAmt47whAaRkRu/8kePM+X8760UGU0RMwGti6Z9y3LQ0RvK6I0brXmbGB/RsN38PVnhcP8ZfxGUH26kX0RK+tlrxcrG+HkPYOH4XPAL8Q1lu1n9x3tLcIPxq8ZZtuIyKYEmoLKyMsvTviG5flTpDprT25unWgE4md1kthRWXOnfWHATVY7Y/r4obiOL1mS5bEa/iNKotQNnvIAKtjBM4RlIDWMa6dmz+lHtLtqDD2LF1qwoiSIHI75LQZ/CNYaHCfZSxtOydpNKq8eb1/PGiLNolD4La2zf0/1dlcr5mkesV570NxRmU1tFm8Zd3MZlZmyv9QIDAQAB-----END PUBLIC KEY-----";

        Jwt token = JwtHelper.decodeAndVerify(jwt, new RsaVerifier(publicKey));
        String claims = token.getClaims();
        System.out.println(claims);

    }
}
