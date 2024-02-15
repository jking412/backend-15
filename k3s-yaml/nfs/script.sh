kubectl apply -f mypv.yaml
kubectl apply -f mypvc.yaml
kubectl apply -f nfs-server-deployment.yaml
kubectl apply -f nfs-server-service.yaml
kubectl apply -f nfs-pv.yaml
kubectl apply -f nfs-pvc.yaml
kubectl apply -f uos.yaml