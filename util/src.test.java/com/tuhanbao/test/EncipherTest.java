package com.tuhanbao.test;

import org.junit.Assert;
import org.junit.Test;

import com.tuhanbao.base.util.encipher.Encipher;
import com.tuhanbao.base.util.encipher.EncipherType;
import com.tuhanbao.base.util.objutil.RandomUtil;

public class EncipherTest extends ITest {
    @Test
    public void test() {
        String oldText = RandomUtil.randomLetterAndNumberString(100);
        info(oldText);

        String password = RandomUtil.randomLetterAndNumberString(8);
        Encipher.resetPassword(password);
        for (EncipherType item : EncipherType.values()) {
            byte[] encrypt = Encipher.encrypt(item, oldText.getBytes());
            info(item.name() + " encrypt :" + new String(encrypt));
            byte[] decrypt = Encipher.decrypt(item, encrypt);
            info(item.name() + " decrypt :" + new String(decrypt));
            Assert.assertEquals(oldText, new String(decrypt));
        }
    }
}
