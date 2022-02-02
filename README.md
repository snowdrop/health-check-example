# Health Check Spring Boot Example

https://appdev.openshift.io/docs/spring-boot-runtime.html#mission-health-check-spring-boot

## Table of Contents

* [Health Check Spring Boot Example](#health-check-spring-boot-example)
    * [Deploying application on OpenShift using Dekorate](#deploying-application-on-openshift-using-dekorate)
    * [Deploying application on OpenShift using Helm](#deploying-application-on-openshift-using-helm)
    * [Running Tests on OpenShift using Dekorate](#running-tests-on-openshift-using-dekorate)
    * [Running Tests on OpenShift using S2i from Source](#running-tests-on-openshift-using-s2i-from-source)
    * [Running Tests on OpenShift using Helm](#running-tests-on-openshift-using-helm)

## Deploying application on OpenShift using Dekorate

```
mvn clean verify -Popenshift -Ddekorate.deploy=true
```

## Deploying application on OpenShift using Helm

First, make sure you have installed [the Helm command line](https://helm.sh/docs/intro/install/) and connected/logged to a kubernetes cluster.

Then, you need to install the example by doing:

```
helm install healthcheck ./helm --set spring-boot-example-app.s2i.source.repo=https://github.com/snowdrop/health-check-example --set spring-boot-example-app.s2i.source.ref=<branch-to-use>
```

**note**: Replace `<branch-to-use>` with one branch from `https://github.com/snowdrop/health-check-example/branches/all`.

And to uninstall the chart, execute:

```
helm uninstall healthcheck
```

## Running Tests on OpenShift using Dekorate

```
./run_tests_with_dekorate.sh
```

## Running Tests on OpenShift using S2i from Source

```bash
./run_tests_with_s2i.sh
```

This script can take 2 parameters referring to the repository and the branch to use to source the images from.

```bash
./run_tests_with_s2i.sh "https://github.com/snowdrop/health-check-example" branch-to-test
```

## Running Tests on OpenShift using Helm

```
./run_tests_with_helm.sh
```

This script can take 2 parameters referring to the repository and the branch to use to source the images from.

```bash
./run_tests_with_helm.sh "https://github.com/snowdrop/health-check-example" branch-to-test
```
