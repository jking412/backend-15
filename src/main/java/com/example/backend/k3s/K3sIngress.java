package com.example.backend.k3s;

import com.example.backend.configure.Constants;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.V1Networking;
import lombok.Data;

import java.util.List;


@Data
public class K3sIngress {

    private V1Ingress ingress;

    private static final String ingressName = "os-ingress";
    //    apiVersion: networking.k8s.io/v1
    //    kind: Ingress
    //    metadata:
    //    name: os-ingress
    //
    //    spec:
    //    rules:
    //            - http:
    //    paths:
    //            - path: /env
    //    pathType: Prefix
    //    backend:
    //    service:
    //    name:  ngx-svc
    //    port:
    //    number: 80
    public K3sIngress(){
        //    apiVersion: networking.k8s.io/v1
        //    kind: Ingress
        this.ingress = new V1Ingress();
        ingress.setApiVersion(Constants.K8S_API_VERSION);
        ingress.setKind(Constants.INGRESS_KIND);

        //    metadata:
        //      name: os-ingress
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName(Constants.INGRESS_NAME);
        ingress.setMetadata(meta);

        // spec:
        V1IngressSpec spec = new V1IngressSpec();

        {
            // rules:
            // - http:
            V1HTTPIngressRuleValue rule = new V1HTTPIngressRuleValue();

            //  paths:
            //  - path: /env
            //  pathType: Prefix
            V1HTTPIngressPath path = new V1HTTPIngressPath();
            path.setPath("/env");
            path.setPathType(Constants.DEFAULT_PATH_TYPE);

            {
                //  backend:
                //  service:
                //  name:  ngx-svc
                V1IngressBackend backend = new V1IngressBackend();
                V1IngressServiceBackend serviceBackend = new V1IngressServiceBackend();
                serviceBackend.setName(Constants.NGX_SVC);
                {
                    //   port:
                    //   number: 80
                    V1ServiceBackendPort serviceBackendPort = new V1ServiceBackendPort();
                    serviceBackendPort.setNumber(Constants.DEFAULT_NGX_SERVICE_PORT);
                    serviceBackend.setPort(serviceBackendPort);
                }

                backend.setService(serviceBackend);

                // set path backend
                path.setBackend(backend);
            }

            // set paths
            rule.setPaths(List.of(path));

            // set rule
            spec.setRules(List.of(new V1IngressRule().http(rule)));
        }
        // set spec
        ingress.setSpec(spec);
    }

    public void create(NetworkingV1Api api, String namespace) throws Exception {
        api.createNamespacedIngress(namespace,ingress,null,null,null,null);
    }

    public void delete(NetworkingV1Api api, String namespace) throws Exception {
        api.deleteNamespacedIngress(Constants.INGRESS_NAME,namespace,null,null,null,null,null,null);
    }
}
