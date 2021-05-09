package brownbear;

import honey.Honey;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class BrownBear {

    private Honey honey;

    public BrownBear(Honey honey) {
        this.honey = honey;
    }
    
    public void eat() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException, IOException {


        try {
            new Socket("google.com", 80);
            System.out.println("BrownBear.eat(): google.com:80 socket open (should have been blocked)");
        } catch (AccessControlException e) {
            System.out.println("BrownBear.eat(): google.com:80 socket blocked by security manager' (correct)");
        } catch (IOException e) {
            throw new RuntimeException("no network?", e);
        }

        try {
            new Socket("yahoo.com", 80);
            System.out.println("BrownBear.eat(): yahoo.com:80 socket open' (correct)");
        } catch (AccessControlException e) {
            e.printStackTrace();
            System.out.println("BrownBear.eat(): yahoo.com:80 socket blocked by security manager (should not have been blocked)");
        } catch (IOException e) {
            throw new RuntimeException("no network?", e);
        }

        ClassLoader brownBearClassloader = this.getClass().getClassLoader(); // will never fail
        System.out.println("BrownBear.eat(): Can access classloader of 'this' class (correct)");

        brownBearClassloader = BrownBear.class.getClassLoader(); // will never fail
        System.out.println("BrownBear.eat(): Can access classloader of BrownBear class (correct)");

        System.out.println("BrownBear.eat(): invoking honey.eatSome()  returns " + honey.eatSome() + " calories of Honey (interface method)");

        try {
            brownBearClassloader.loadClass("beehivehoney.BeeHiveHoney");
            System.out.println("BrownBear.eat(): Can see beehivehoney.BeeHiveHoney.class - shouldn't have worked - that class is not in the classloader hierarchy for BrownBear");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("BrownBear.eat(): Can't see beehivehoney.BeeHiveHoney.class at all? - correct, that is not in classloader hierarchy for BrownBear");
        }

        System.out.println("BrownBear.eat(): honey instance's class type - " + honey.getClass());
        Method nonInterfaceMethod = null;
        try {
            nonInterfaceMethod = honey.getClass().getMethod("nonInterfaceMethod");
            System.out.println("BrownBear.eat(): Can see 'honey' instance's nonInterfaceMethod() - should have been impossible as that method isn't on HoneyImplHider");
            nonInterfaceMethod.invoke(honey); // will never get here - exception route correctly taken instead
        } catch (NoSuchMethodException exception) {
            System.out.println("BrownBear.eat(): Can't see 'honey' instance's nonInterfaceMethod() - correct, not on HoneyImplHider class (only on BeeHiveHoney class)");
        }

        try {
            Object hiddenImpl = honey.getClass().getDeclaredFields()[0].get(honey);
            System.out.println("BrownBear.eat(): field HoneyImplHider.hiddenImpl class name is " + hiddenImpl.getClass() + " (security manager should block this)");
        } catch (AccessControlException e) {
            System.out.println("BrownBear.eat(): field HoneyImplHider.hiddenImpl reflection access is correctly blocked by security manager");
        }

        new DynamicAttemptToMakeBrownBear().go(honey);

    }
    
}