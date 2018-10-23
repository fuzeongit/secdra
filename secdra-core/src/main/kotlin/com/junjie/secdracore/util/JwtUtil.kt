package com.junjie.secdracore.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

object JwtUtil {
    fun parseJWT(jsonWebToken: String, base64Security: String): Claims {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).body
        } catch (e: Exception) {
            throw e
        }
    }

    fun createJWT(userId: String, expiresSecond: Long, base64Security: String): String {
        val signatureAlgorithm = SignatureAlgorithm.HS256

        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)

        //生成签名密钥
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security)
        val signingKey = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)


        //添加构成JWT的参数
        val builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("userId", userId)
                .signWith(signatureAlgorithm, signingKey)
        //添加Token过期时间
        if (expiresSecond >= 0) {
            val expMillis = nowMillis + expiresSecond
            val exp = Date(expMillis)
            builder.setExpiration(exp).setNotBefore(now)
        }

        //生成JWT
        return builder.compact()
    }
}