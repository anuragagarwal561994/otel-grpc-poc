# otel-grpc-poc

## Project Setup Guide:

```bash
## Install sdkman for installing java
brew install sdkman

## Install Istio
curl -L https://istio.io/downloadIstio | sh -
cd istio-1.16.1
export PATH=$PWD/bin:$PATH
istioctl install --set profile=demo -y
kubectl create namespace grpc-istio
kubectl label namespace default istio-injection=enabled

## Build Jar & Docker Images
git clone git@github.com:anuragagarwal561994/otel-grpc-poc.git
cd otel-grpc-poc
sdkman env install
sdkman env
./mvnw clean install

## Setup Istio Configuration
kubectl apply -f src/main/kubernetes

## (Optional) expose kubernetes services on local
brew install kubefwd
curl http://grpc-client:8080/ipservice/grpc

## Or Else
export INGRESS_HOST=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].port}')
export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT
curl http://$GATEWAY_URL/ipservice/grpc
```

Attached in .run are the configurations to debug locally as well as to start a debugger in kubernetes.

These should get loaded in IntelliJ IDE

![Screenshot 2022-12-19 at 10.33.59 PM.png](Screenshot%202022-12-19%20at%2010.33.59%20PM.png)


## Project Cleanup Guide

```bash
## Remove Istio Configuration
kubectl delete -f src/main/kubernetes

## Remove istio setup
istioctl uninstall -y --purge

## Remove namespace
kubectl delete namespace grpc-istio
```
