import java.security.MessageDigest;

import com.tuhanbao.base.util.encipher.EncipherType;
import com.tuhanbao.base.util.encipher.EncipherUtil;
import com.tuhanbao.base.util.encipher.SHAUtil;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.excel.ExcelUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.ByteUtil;

public class test {
    public static void main(String args[]) {
//        System.out.println(SHAUtil.getSHA("hah"));
        String s = EncipherUtil.encrypt(EncipherType.DES, "我们一起");
        System.out.println(s);
        System.out.println(EncipherUtil.decrypt(EncipherType.AES, s));
    }
}   
