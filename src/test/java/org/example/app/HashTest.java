package org.example.app;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashTest {
    @Test
    public void testHashing() {
        String password = "maslo123";
        String hashed= DigestUtils.sha256Hex(password);
        String expected = "baca21721c6f2cce47ad8070fc9a1d777db736c2ae44d7aedc9eacf7aa2658b6";
        assertEquals(expected, hashed );
    }
}