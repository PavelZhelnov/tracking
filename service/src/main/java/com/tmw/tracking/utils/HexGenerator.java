package com.tmw.tracking.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Hex generator
 * @author dmikhalishin@provectus-it.com
 */
public class HexGenerator {

    private final static Logger logger = LoggerFactory.getLogger(HexGenerator.class);

    private static String convertToHex(final byte[] data) {
        final StringBuilder hexString = new StringBuilder();
        for (byte aData : data) {
            String hex = Integer.toHexString(0xff & aData);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String md5(final String text) {
        if(text == null) {
            return null;
        }
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            return convertToHex(md.digest());
        } catch(Exception e) {
            logger.error(Utils.errorToString(e));
        }
        return null;
    }
}
