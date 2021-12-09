# Health Check Spring Boot Example

## Table of Contents

* [Health Check Spring Boot Example](#health-check-spring-boot-example)
    * [Deploying application on OpenShift using Dekorate:](#deploying-application-on-openshift-using-dekorate)
    * [Running Tests on OpenShift using Dekorate:](#running-tests-on-openshift-using-dekorate)
    * [Running Tests on OpenShift using S2i from Source:](#running-tests-on-openshift-using-s2i-from-source)
    * [Running Tests on Kubernetes with External Registry:](#running-tests-on-kubernetes-with-external-registry)


https://appdev.openshift.io/docs/spring-boot-runtime.html#mission-health-check-spring-boot

## Deploying application on OpenShift using Dekorate:

```
mvn clean verify -Popenshift -Ddekorate.push=true
```

## Running Tests on OpenShift using Dekorate:

```
./run_tests_with_dekorate.sh
```

## Running Tests on OpenShift using S2i from Source:

```bash
./run_tests_with_s2i.sh "https://github.com/snowdrop/health-check-example" sb-2.4.x
```

This script can take 2 parameters referring to the repository and the branch to use to source the images from. 
The parameters default to `https://github.com/snowdrop/health-check-example` and  `sb-2.4.x` if the script is executed without parameters. 

```bash
./run_tests_with_s2i.sh 
```

## Running Tests on Kubernetes with External Registry:

```
mvn clean verify -Pkubernetes,kubernetes-it -Ddekorate.docker.registry=<url to your registry, example: quay.io> -Ddekorate.push=true
```
