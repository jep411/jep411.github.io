Proof of concept security manager in plain Java (8+)

Seven Java source files, to bash scripts and one skinny 'policy' file

## First compile everything

```
./build.sh
```

We chose not to choose Maven, Gradle or other.

## To test

```
./run.sh
```

Poke around the source files. Particularly Main.java and BrownBear.java's eat() method.

## Classloader hierarchy (set up by main.Main.main() class)

```
   +-------------------+  +---------------+
   |                   |  |               |
   |  Classloader      |  | Classloader   |
   |  containing       |  | containing    |
   |  beehivehoney.jar |  | brownbear.jar |
   |                   |  |               |
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


