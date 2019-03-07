package com.iwxyi.letsremember.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    /**
     * 将输入流转换到文本
     * @param in 输入流
     * @return 文本
     * @throws IOException
     */
    public static String readStream(InputStream in) throws IOException {
        int len = -1;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ( (len = in.read(buffer)) != -1 ) {
            baos.write(buffer, 0, len);
        }
        in.close();
        String content = new String(baos.toByteArray());
        return content;
    }
}
