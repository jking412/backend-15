#! /bin/bash

chmod 644 /etc/rancher/k3s/k3s.yaml
cp /etc/rancher/k3s/k3s.yaml /home/jking/.kube/config
cp /home/jking/.kube/config $(pwd)/config
mv ./config ../config