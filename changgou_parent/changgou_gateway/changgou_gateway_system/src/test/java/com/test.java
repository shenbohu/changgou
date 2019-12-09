package com;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class test {
    public static void main(String[] args) {
        // 盐
        String gensalt = BCrypt.gensalt();
        System.out.println(gensalt);
        //加密
        String hashpw = BCrypt.hashpw("123456", gensalt);
        System.out.println(hashpw);

        //解密
        boolean checkpw = BCrypt.checkpw("12345", hashpw);
        System.out.println(checkpw);
    }
}
