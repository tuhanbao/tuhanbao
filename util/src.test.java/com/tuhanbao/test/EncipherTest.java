package com.tuhanbao.test;

import org.junit.Assert;
import org.junit.Test;

import com.tuhanbao.base.util.encipher.EncipherUtil;
import com.tuhanbao.base.util.encipher.EncipherType;
import com.tuhanbao.base.util.objutil.RandomUtil;

public class EncipherTest extends ITest {
    @Test
    public void test() {
        String oldText = RandomUtil.randomLetterAndNumberString(100);
        info(oldText);

        String password = RandomUtil.randomLetterAndNumberString(8);
        EncipherUtil.resetPassword(password);
        for (EncipherType item : EncipherType.values()) {
            byte[] encrypt = EncipherUtil.encrypt(item, oldText.getBytes());
            info(item.name() + " encrypt :" + new String(encrypt));
            byte[] decrypt = EncipherUtil.decrypt(item, encrypt);
            info(item.name() + " decrypt :" + new String(decrypt));
            Assert.assertEquals(oldText, new String(decrypt));
        }
    }
}
