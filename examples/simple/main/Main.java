package main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;

import static main.CustomPermissionsURLClassLoader.makePermissions;

public class Main {
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {

        URL honeyUrl = new File("honey.jar").toURL();
        System.out.println("> hurl " + honeyUrl);
        final CustomPermissionsURLClassLoader honeyCL = new CustomPermissionsURLClassLoader(new URL[] {
                honeyUrl
        }, new HashMap(), Main.class.getClassLoader().getParent());

        CustomPermissionsURLClassLoader beehivehoneyCL = new CustomPermissionsURLClassLoader(new URL[] {
                new File("beehivehoney.jar").toURL()
        }, new HashMap(), honeyCL);

        final ClassPathElement brownbearCpe = new ClassPathElement(new File("brownbear.jar").toURL());
        brownbearCpe.grantPermission(new java.net.SocketPermission("yahoo.com:80", "connect"));


        ClassLoader brownbearCL = AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () ->
                new CustomPermissionsURLClassLoader(new URL[] {brownbearCpe.getUrl()},
                makePermissions(new ClassPathElement[] {brownbearCpe}), honeyCL)
        );

        Object beehivehoney = beehivehoneyCL.loadClass("beehivehoney.BeeHiveHoney").newInstance();

        // There's only one constructor
        Object honey = honeyCL.loadClass("honey.HoneyImplHider").getConstructors()[0]
                .newInstance(beehivehoney);

        // There's only one constructor
        Object brownbear = brownbearCL.loadClass("brownbear.BrownBear").getConstructors()[0]
                .newInstance(honey);

        // Call BrownBear.eat(), the only method
        brownbear.getClass().getDeclaredMethods()[0].invoke(brownbear);

    }
}