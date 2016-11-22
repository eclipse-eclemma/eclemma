#!/bin/bash

set -euo pipefail

if [[ ${TRAVIS_PULL_REQUEST} == 'false' && ${TRAVIS_BRANCH} == 'master' && ${PLATFORM} == 'e3.8' ]]
then
  mvn -V -B -e \
    verify -Djdk.version=${JDK} --toolchains=./.travis-toolchains.xml \
    sonar:sonar -Dsonar.host.url=${SONARQUBE_URL} -Dsonar.login=${SONARQUBE_TOKEN} \
    -P ${PLATFORM}
else
  mvn -V -B -e \
    verify -Djdk.version=${JDK} --toolchains=./.travis-toolchains.xml \
    -P ${PLATFORM}
fi
