#!/usr/bin/env bash
CONTAINER_REGISTRY=${1:-localhost:5000}
K8S_NAMESPACE=${2:-genhelm}

source scripts/waitFor.sh
oc project $K8S_NAMESPACE

# Build
./mvnw -s .github/mvn-settings.xml clean package -Pkubernetes,helm -DskipTests -Ddekorate.options.properties-profile=helm

# Create docker image and tag it in registry
IMAGE=health-check:genhelm
docker build . -t $IMAGE
docker tag $IMAGE $CONTAINER_REGISTRY/$IMAGE
docker push $CONTAINER_REGISTRY/$IMAGE

helm install health-check ./target/classes/META-INF/dekorate/helm/health-check --set app.image=$CONTAINER_REGISTRY/$IMAGE -n $K8S_NAMESPACE
if [[ $(waitFor "health-check" "app.kubernetes.io/name") -eq 1 ]] ; then
  echo "Application failed to deploy. Aborting"
  exit 1
fi

# Run Tests
./mvnw -s .github/mvn-settings.xml clean verify -Pkubernetes-it -Dunmanaged-test=true -Dkubernetes.namespace=$K8S_NAMESPACE
