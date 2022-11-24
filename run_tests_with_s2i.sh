#!/usr/bin/env bash
# Parameters allowed:
#   . --repository-url
#   . --branch-to-test
#   . --maven-settings
#   . --maven-mirror-url
SOURCE_REPOSITORY_URL="https://github.com/snowdrop/health-check-example"
SOURCE_REPOSITORY_REF="sb-2.7.x"
MAVEN_SETTINGS_REF=""
MAVEN_MIRROR_URL=""

while [ $# -gt 0 ]; do
  if [[ $1 == *"--"* ]]; then
    param="${1/--/}"
    case $1 in
      --repository-url) SOURCE_REPOSITORY_URL="$2";;
      --branch-to-test) SOURCE_REPOSITORY_REF="$2";;
      --maven-settings) MAVEN_SETTINGS_REF="-s $2";;
      --maven-mirror-url) MAVEN_MIRROR_URL="$2";;
    esac;
  fi
  shift
done

source scripts/waitFor.sh

oc create -f .openshiftio/application.yaml
oc new-app --template=health-check -p SOURCE_REPOSITORY_URL=$SOURCE_REPOSITORY_URL -p SOURCE_REPOSITORY_REF=$SOURCE_REPOSITORY_REF -p MAVEN_MIRROR_URL=$MAVEN_MIRROR_URL
if [[ $(waitFor "health-check" "app") -eq 1 ]] ; then
  echo "Application failed to deploy. Aborting"
  exit 1
fi

# Run Tests
eval "./mvnw ${MAVEN_SETTINGS_REF} clean verify -Popenshift,openshift-it -Dunmanaged-test=true"
