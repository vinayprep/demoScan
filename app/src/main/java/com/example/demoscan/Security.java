package com.example.demoscan;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.codec.binary.Hex;

import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


class Security {

    private static String edKey = "w3h84fFfrwpKExMXimYom810303FNb64";

    public static String encrypt(String plaintext) throws Exception {
        byte[] keyValue = edKey.getBytes();
        Key key = new SecretKeySpec(keyValue, "AES");
        //serialize
        String serializedPlaintext = "s:" + plaintext.getBytes().length + ":\"" + plaintext + "\";";
        byte[] plaintextBytes = serializedPlaintext.getBytes("UTF-8");

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = c.getIV();
        byte[] encVal = c.doFinal(plaintextBytes);
        String encryptedData = Base64.encodeToString(encVal, Base64.NO_WRAP);

        SecretKeySpec macKey = new SecretKeySpec(keyValue, "HmacSHA256");
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(macKey);
        hmacSha256.update(Base64.encode(iv, Base64.NO_WRAP));
        byte[] calcMac = hmacSha256.doFinal(encryptedData.getBytes("UTF-8"));
        String mac = new String(Hex.encodeHex(calcMac));
        //Log.d("MAC",mac);

        CustomDecryption aesData = new CustomDecryption(
                Base64.encodeToString(iv, Base64.NO_WRAP),
                encryptedData,
                mac);

        String aesDataJson = new Gson().toJson(aesData);

        return Base64.encodeToString(aesDataJson.getBytes("UTF-8"), Base64.DEFAULT);
    }

    public static String decrypt(String input) throws Exception {
        byte[] keyValue = edKey.getBytes();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        CustomDecryption data;
        try {
            data = gson.fromJson(new String(Base64.decode(input, Base64.DEFAULT)), CustomDecryption.class);
        } catch (Exception e) {
            data = new CustomDecryption();
            e.printStackTrace();
        }
        Key key = new SecretKeySpec(keyValue, "AES");
        byte[] iv = Base64.decode(data.getIv().getBytes("UTF-8"), Base64.DEFAULT);
        byte[] decodedValue = Base64.decode(data.getValue().getBytes("UTF-8"), Base64.DEFAULT);

        SecretKeySpec macKey = new SecretKeySpec(keyValue, "HmacSHA256");
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(macKey);
        hmacSha256.update(data.getIv().getBytes("UTF-8"));
        byte[] calcMac = hmacSha256.doFinal(data.getValue().getBytes("UTF-8"));
        byte[] mac = Hex.decodeHex(data.getMac().toCharArray());
        if (!Arrays.equals(calcMac, mac))
            return "MAC mismatch";

        Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding"); // or PKCS5Padding
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decValue = c.doFinal(decodedValue);

        int firstQuoteIndex = 0;
        while (decValue[firstQuoteIndex] != (byte) '"') firstQuoteIndex++;
        return new String(Arrays.copyOfRange(decValue, firstQuoteIndex + 1, decValue.length - 2));
    }
}