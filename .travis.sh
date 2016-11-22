#!/bin/bash

set -euo pipefail

export DISPLAY=:99.0
sh -e /etc/init.d/xvfb start
sleep 5
metacity --sm-disable --replace 2> metacity.err &

if [[ ${TRAVIS_PULL_REQUEST} == 'false' && ${TRAVIS_BRANCH} == 'master' && ${PLATFORM} == 'e3.8' ]]
then
  mvn -V -B -e \
    verify -Djdk.version=${JDK} --toolchains=./.travis-toolchains.xml \
    sonar:sonar -Dsonar.host.url=${SONARQUBE_URL} -Dsonar.login=${SONARQUBE_TOKEN} \
    -P ${PLATFORM} -DskipUITests=false
else
  mvn -V -B -e \
    verify -Djdk.version=${JDK} --toolchains=./.travis-toolchains.xml \
    -P ${PLATFORM} -DskipUITests=false
fi
