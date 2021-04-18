#!/bin/bash

java -Djava.security.manager -Djava.security.policy=file:sandbox.policy -cp main.jar "main/Main"