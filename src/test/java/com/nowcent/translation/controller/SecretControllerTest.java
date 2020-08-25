package com.nowcent.translation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcent.translation.pojo.SecretPojo;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author orangeboy
 * @version 1.0
 * @date 2020/8/5 20:58
 */
class SecretControllerTest {

    String src = "{\"a\":\"sfsafsafasdfgtrklyjghlewkjfqw\",\"b\":\"jklasjfl;asjfljeorghjkefjalfdsja;fj\"}";

    @Test
    void test() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException {
        //1.生成key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("des");//密钥生成器
        keyGenerator.init(56);//指定密钥长度为56位
        SecretKey secretKey = keyGenerator.generateKey();//用密钥生成器生成密钥
        byte[] byteKeys = secretKey.getEncoded();//得到密钥的byte数组

        String secret = new String(byteKeys);
        System.out.println(secret);

        //2.key转换
        DESKeySpec desKeySpec = new DESKeySpec(byteKeys);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("des");//秘密密钥工厂
        SecretKey convertSecretKey = factory.generateSecret(desKeySpec);

        //3.加密
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");//见上图的工作模式和填充模式
        cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);//加密模式
        byte[]result = cipher.doFinal(src.getBytes());

        System.out.println("jdk DES加密：\n" + Hex.encodeHexString(result));

        //4.解密
        cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);//解密模式
        result = cipher.doFinal(result);
        System.out.println("jdk DES解密：\n" + new String(result));

    }

    @Test
    void Test2() throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {
        byte[] rawKey = generateSecret();
        System.out.println("rawKey=>" + new String(rawKey));
        byte[] key = base64Encode(rawKey);
        System.out.println("key=>" + new String(key));
//        byte[] encryptData = encrypt(JSON.toJSONString(new SecretPojo()), rawKey);
        byte[] encryptData = encrypt(src, rawKey);
        System.out.println("end=>" + Hex.encodeHexString(encryptData));



        byte[] key2 = base64Decode(key);
        System.out.println("key2=>" + new String(key2));

        byte[] decrypt = decrypt(encryptData, key2);
        System.out.println("de=>" + Hex.encodeHexString(decrypt));

        JSONObject decryptData = JSON.parseObject(new String(decrypt));
        String APP_ID = decryptData.getString("appId");
        String SECRET_KEY = decryptData.getString("secretKey");

        System.out.println(APP_ID);
        System.out.println(SECRET_KEY);


    }




    private byte[] generateSecret() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("des");
        keyGenerator.init(56);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    private byte[] encrypt(String raw, byte[] secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        DESKeySpec desKeySpec = new DESKeySpec(secret);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("des");
        SecretKey convertSecretKey = factory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
        return cipher.doFinal(raw.getBytes());
    }

    private byte[] decrypt(byte[] raw, byte[] secret) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        DESKeySpec desKeySpec = new DESKeySpec(secret);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("des");
        SecretKey convertSecretKey = factory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
        return cipher.doFinal(raw);
    }

    private byte[] base64Encode(byte[] raw){
        return Base64.getEncoder().encode(raw);
    }

    private byte[] base64Decode(byte[] raw){
        return Base64.getDecoder().decode(raw);
    }

}