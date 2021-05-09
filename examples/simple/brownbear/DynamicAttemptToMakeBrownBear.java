package brownbear;

import honey.Honey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class DynamicAttemptToMakeBrownBear {

    public void go(Honey honey) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {

        System.out.println("==== Stage 1, Try to make a class loader to bust out of the sandbox ==== ");

        ClassLoader brownbearCL2 = null;
        try {
            brownbearCL2 = AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () ->
                    {
                        try {
                            URL bbJar = new File("brownbear.jar").toURL();
                            //URL bbJar = new File("/Volumes/OHD/scm/oss/jep411.github.io/examples/simple/brownbear.jar").toURL();
                            return new URLClassLoader(new URL[]{bbJar}, this.getClass().getClassLoader());
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            throw new RuntimeException("should have failed here with access denied");
        } catch (java.security.AccessControlException e) {
            if (e.getMessage().startsWith("access denied (\"java.io.FilePermission\"") ||
            e.getMessage().startsWith("access denied (\"java.util.PropertyPermission\" \"user.dir\" \"read\")")) {
                System.out.println("Access of 'brownbear.jar' expected to be blocked - access denied, and was.");
            } else {
                throw new RuntimeException(e);
            }
        }

        byte[] brownBearClassData = getBrownBearClassBytes();
        brownbearCL2 = new AClassLoader(brownBearClassData);

        // There's only one constructor
        Object brownbear2 = brownbearCL2.loadClass("brownbear.BrownBear").getConstructors()[0]
                .newInstance(honey);

        // Call BrownBear.eat(), the only method
        brownbear2.getClass().getDeclaredMethods()[0].invoke(brownbear2);

    }

    private byte[] getBrownBearClassBytes() throws IOException {
        InputStream brownBearClassInputStream = BrownBear.class.getResourceAsStream("brownbear/BrownBear.class");
        System.out.println("brownBearClassInputStream not null: " + (brownBearClassInputStream != null));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int data = brownBearClassInputStream.read();

        while(data != -1){
            buffer.write(data);
            data = brownBearClassInputStream.read();
        }

        brownBearClassInputStream.close();

        return buffer.toByteArray();
    }

    private static class AClassLoader extends ClassLoader {
        private byte[] brownBearClassData;

        public AClassLoader(byte[] brownBearClassData) {

            this.brownBearClassData = brownBearClassData;
        }

        public Class loadClass(String name) throws ClassNotFoundException {
            if (!"brownbear.BrownBear".equals(name)) {
                System.out.println("MyCL = " + name);
                return super.loadClass(name);
            }
            System.out.println("EEEEEE");
            return defineClass("brownbear.BrownBear", brownBearClassData, 0, brownBearClassData.length);
        }
    }
}
