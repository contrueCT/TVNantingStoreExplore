import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
public class Testjjwt {

    public static void main(String[] args) {
        // 创建密钥
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // 生成 JWT
        String jwt = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1小时有效期
                .signWith(key)
                .compact();

        System.out.println("Generated JWT: " + jwt);

        // 解析 JWT
        String parsedSubject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();

        System.out.println("Parsed Subject: " + parsedSubject);
    }

}
