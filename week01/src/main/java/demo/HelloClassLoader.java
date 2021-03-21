package demo;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;

public class HelloClassLoader extends ClassLoader {
    private static final String FILE_NAME = "/Hello.xlass";
    private static final byte[] EMPTY = new byte[0];

    public static void main(String[] args) throws IOException {
        HelloClassLoader cl = new HelloClassLoader();
        try {
            Class<?> helloClazz = cl.findClass("Hello");
            Object obj = helloClazz.newInstance();
            Method helloMethod = helloClazz.getDeclaredMethod("hello");
            helloMethod.setAccessible(true);
            helloMethod.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = readClassBytes(FILE_NAME);
        byte[] rawClassBytes = decrypt(classBytes);
        return defineClass(name, rawClassBytes, 0, rawClassBytes.length);
    }


    private byte[] readClassBytes(String filePath) {
        URL fileUrl = this.getClass().getResource(filePath);
        if (fileUrl == null) {
            throw new RuntimeException("file not found");
        }
        File file = new File(fileUrl.getPath());
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            return EMPTY;
        }
        byte[] fileBytes = new byte[(int) fileSize];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    private byte[] decrypt(byte[] rawBytes) {
        byte[] ret = new byte[rawBytes.length];
        int len = rawBytes.length;
        for (int i = 0; i < len; i++) {
            ret[i] = (byte) (255 - rawBytes[i]);
        }
        return ret;
    }
}
