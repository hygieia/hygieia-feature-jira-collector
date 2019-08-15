#!/bin/bash

cp src/devops/.travis.settings.xml $HOME/.m2/settings.xml

openssl aes-256-cbc -K $encrypted_6384b16821a6_key -iv $encrypted_6384b16821a6_iv -in src/devops/keys.gpg.enc -out keys.gpg -d

gpg --fast-import keys.gpg

shred keys.gpg

sed -i 's|-SNAPSHOT||g' pom.xml

mvn deploy -q -P release

# add release notes step
