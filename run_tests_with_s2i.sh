#!/usr/bin/env bash
SOURCE_REPOSITORY_URL=${1:-https://github.com/snowdrop/health-check-example}
SOURCE_REPOSITORY_REF=${2:-sb-2.4.x}

oc create -f .openshiftio/application.yaml
oc new-app --template=health-check -p SOURCE_REPOSITORY_URL=$SOURCE_REPOSITORY_URL -p SOURCE_REPOSITORY_REF=$SOURCE_REPOSITORY_REF

sleep 30 # needed in order to bypass the 'Pending' state
# wait for the app to stand up
timeout 300s bash -c 'while [[ $(oc get pod -o json | jq  ".items[] | select(.metadata.name | contains(\"build\"))  | .status  " | jq -rs "sort_by(.startTme) | last | .phase") == "Running" ]]; do sleep 20; done; echo ""'

# launch the tests without deploying the application
./mvnw -s .github/mvn-settings.xml clean verify -Popenshift,openshift-it -Dunmanaged-test=true
