#!/bin/bash
javac honey/Honey.java
javac honey/HoneyImplHider.java
jar cvf honey.jar "honey/Honey.class"
jar -uf honey.jar "honey/HoneyImplHider.class"
javac -cp honey.jar beehivehoney/BeeHiveHoney.java
jar cvf beehivehoney.jar "beehivehoney/BeeHiveHoney.class"
javac -cp honey.jar brownbear/BrownBear.java
jar cvf brownbear.jar "brownbear/BrownBear.class"
javac main/CustomPermissionsURLClassLoader.java
javac main/ClassPathElement.java
javac main/Main.java
jar cvf main.jar "main/CustomPermissionsURLClassLoader.class"
jar uf main.jar "main/CustomPermissionsURLClassLoader\$1.class"
jar uf main.jar "main/ClassPathElement.class"
jar uf main.jar "main/Main.class"
jar uf main.jar "main/Main\$1.class"