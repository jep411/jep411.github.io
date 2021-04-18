Proof of concept security manager in plain Java (8+)

Seven Java source files, to bash scripts and one skinny 'policy' file

## First compile everything

```
cd examples/simple
./build.sh
```

We chose not to choose Maven, Gradle or other.

## To test

```
./run.sh
```

Command line output should a bunch of tests in series and should look like:

```
$ ./run.sh 
BrownBear.eat(): google.com:80 socket blocked by security manager' (correct)
BrownBear.eat(): yahoo.com:80 socket open' (correct)
BrownBear.eat(): Can access classloader of 'this' class (correct)
BrownBear.eat(): Can access classloader of BrownBear class (correct)
BrownBear.eat(): invoking honey.eatSome()  returns 0.2555535048170051 calories of Honey (interface method)
BrownBear.eat(): Can't see beehivehoney.BeeHiveHoney.class at all? - correct, that is not in classloader hierarchy for BrownBear
BrownBear.eat(): honey instance's class type - class honey.HoneyImplHider
BrownBear.eat(): Can't see 'honey' instance's nonInterfaceMethod() - correct, not on HoneyImplHider class (only on BeeHiveHoney class)
BrownBear.eat(): field HoneyImplHider.hiddenImpl reflection access is correctly blocked by security manager
```

This simple demo sets up BrownBear to access yahoo, but NOT google (or anything else),

Poke around the source files. Particularly:

* Main.java's main() method which does the classloader hierarchy, the security manager and the permissions.
* BrownBear.java's eat() method, which demos the permitted and not permitted stuff.

## Classloader hierarchy (set up by main.Main.main() class)

```
   +-------------------+  +---------------+
   |                   |  |               |
   |  Classloader      |  | Classloader   |
   |  containing       |  | containing    |
   |  beehivehoney.jar |  | brownbear.jar |
   |                   |  |               |
   |                   |  | (allowed to   |
   |                   |  | access yahoo) |
   +-------------------+  +---------------+
                   |          |
             parent|          |parent
                   V          V
               +------------------------+
               |                        |
               | Classloader containing |
               | honey.jar              |
               |                        |
               +------------------------+
                        |
                        |parent
                        V
               +-------------------+
               |                   |
               | JDK's own runtime |
               | classloader       |
               |                   |
               +-------------------+
```
