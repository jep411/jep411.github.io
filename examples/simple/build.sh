#!/bin/bash
javac -g honey/Honey.java honey/HoneyImplHider.java
jar cvf honey.jar "honey/Honey.class"
jar -uf honey.jar "honey/HoneyImplHider.class"
javac -g -cp honey.jar beehivehoney/BeeHiveHoney.java
jar cvf beehivehoney.jar "beehivehoney/BeeHiveHoney.class"
javac -g -cp honey.jar brownbear/BrownBear.java brownbear/DynamicAttemptToMakeBrownBear.java
jar cvf brownbear.jar "brownbear/BrownBear.class"
jar -uf brownbear.jar "brownbear/DynamicAttemptToMakeBrownBear.class"
jar -uf brownbear.jar "brownbear/DynamicAttemptToMakeBrownBear\$AClassLoader.class"
javac -g main/CustomPermissionsURLClassLoader.java main/ClassPathElement.java main/Main.java
jar cvf main.jar "main/CustomPermissionsURLClassLoader.class"
jar uf main.jar "main/CustomPermissionsURLClassLoader\$1.class"
jar uf main.jar "main/ClassPathElement.class"
jar uf main.jar "main/Main.class"
jar uf main.jar "main/Main\$1.class"