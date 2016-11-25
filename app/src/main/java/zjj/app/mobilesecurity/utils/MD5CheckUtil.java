package zjj.app.mobilesecurity.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5CheckUtil {

    public static String getMD5(String realPath){
        FileInputStream fis = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            fis =  new FileInputStream(new File(realPath));
            byte[] buf = new byte[1024];
            int len = 0;
            while((len = fis.read(buf)) != -1){
                digest.update(buf, 0, len);
            }

            StringBuilder builder = new StringBuilder();
            byte[] results = digest.digest();
            for(byte b : results){
                int num = b & 0xff;
                String str = Integer.toHexString(num);
                if(str.length() == 1){
                    builder.append("0" + str);
                }else{
                    builder.append(str);
                }
            }

            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }


}
