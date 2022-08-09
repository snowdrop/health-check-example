#!/usr/bin/env bash
SOURCE_REPOSITORY_URL=${1:-https://github.com/snowdrop/health-check-example}
SOURCE_REPOSITORY_REF=${2:-sb-2.5.x}
S2I_BUILDER_IMAGE_REPO=registry.access.redhat.com/ubi8/openjdk-11
S2I_BUILDER_IMAGE_TAG=1.14

source scripts/waitFor.sh

helm install healthcheck ./helm --set app.route.expose=true --set app.s2i.source.repo=$SOURCE_REPOSITORY_URL --set app.s2i.source.ref=$SOURCE_REPOSITORY_REF --set app.s2i.builderImage.repo=$S2I_BUILDER_IMAGE_REPO --set app.s2i.builderImage.tag=$S2I_BUILDER_IMAGE_TAG
if [[ $(waitFor "health-check" "app") -eq 1 ]] ; then
  echo "Application failed to deploy. Aborting"
  exit 1
fi

# Run OpenShift Tests
./mvnw -s .github/mvn-settings.xml clean verify -Popenshift,openshift-it -Dunmanaged-test=true
