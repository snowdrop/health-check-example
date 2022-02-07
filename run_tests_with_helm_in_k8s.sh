#!/usr/bin/env bash
CONTAINER_REGISTRY=${1:-localhost:5000}
K8S_NAMESPACE=${2:-helm}

# Build
./mvnw -s .github/mvn-settings.xml clean package

# Create docker image and tag it in registry
IMAGE=healthcheck:latest
docker build . -t $IMAGE
docker tag $IMAGE $CONTAINER_REGISTRY/$IMAGE
docker push $CONTAINER_REGISTRY/$IMAGE

helm install healthcheck ./helm --set app.docker.image=$CONTAINER_REGISTRY/$IMAGE -n $K8S_NAMESPACE
if [[ $(kubectl wait --for=condition=ready --timeout=600s pod -l app=health-check -n $K8S_NAMESPACE | grep "condition met" | wc -l) -eq 0 ]] ; then
  echo "Application failed to deploy. Aborting"
  exit 1
fi

# Run OpenShift Tests
./mvnw -s .github/mvn-settings.xml clean verify -Pkubernetes-it -Dunmanaged-test=true -Dkubernetes.namespace=$K8S_NAMESPACE
