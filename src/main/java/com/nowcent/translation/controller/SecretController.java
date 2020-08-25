package com.nowcent.translation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcent.translation.pojo.EncryptPojo;
import com.nowcent.translation.pojo.SecretPojo;
import com.nowcent.translation.vo.ApiVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.binary.Hex;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * @author orangeboy
 * @version 1.0
 * @date 2020/8/5 20:09
 */
@RestController
public class SecretController {

    private String APP_ID = "20200803000532380";
    private String SECRET_KEY = "VQoeTSBUg1YkptcUNeLr";

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/get")
    public ApiVO<?> getSecret(){

        try {
            byte[] rawKey = generateSecret();
            byte[] key = base64Encode(rawKey);
            byte[] encryptData = encrypt(JSON.toJSONString(new SecretPojo()), rawKey);
            return new ApiVO<>(200, null, new EncryptPojo(key, encryptData));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return new ApiVO<>(403, null, null);
        }
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

    private byte[] decrypt(String raw, byte[] secret) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        DESKeySpec desKeySpec = new DESKeySpec(secret);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("des");
        SecretKey convertSecretKey = factory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
        return cipher.doFinal(raw.getBytes());
    }

    private byte[] base64Encode(byte[] raw){
        return Base64.getEncoder().encode(raw);
    }

    private byte[] base64Decode(byte[] raw){
        return Base64.getDecoder().decode(raw);
    }


}
