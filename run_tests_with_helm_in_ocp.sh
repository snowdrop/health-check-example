#!/usr/bin/env bash
SOURCE_REPOSITORY_URL=${1:-https://github.com/snowdrop/health-check-example}
SOURCE_REPOSITORY_REF=${2:-sb-2.5.x}

source scripts/waitFor.sh

helm install healthcheck ./helm -f helm/values-ocp-defaults.yaml --set app.s2i.source.repo=$SOURCE_REPOSITORY_URL --set app.s2i.source.ref=$SOURCE_REPOSITORY_REF
if [[ $(waitFor "health-check" "app") -eq 1 ]] ; then
  echo "Application failed to deploy. Aborting"
  exit 1
fi

# Run OpenShift Tests
./mvnw -s .github/mvn-settings.xml clean verify -Popenshift,openshift-it -Dunmanaged-test=true
