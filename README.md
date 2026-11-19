# Kubernetes microservices demo: Consumer Service

This is a Java 8 / Spring Boot 2.7.18 application. It exposes:

- `GET /api/consumer`

When called, it makes an HTTP GET request to the existing application's:

- `GET /api/info`

It returns the response body from that existing service.

## Assumptions about your existing Info Service

The Kubernetes manifest `k8s/info-service.yaml` assumes:

- Your existing Deployment is in the same Kubernetes namespace (normally `default`).
- Its Pods have the label `app: springboot-demo`.
- Your existing application listens on port `8080` inside its container.
- Its endpoint is available at `/api/info`.

Check these assumptions with:

```powershell
kubectl get pods --show-labels
kubectl get deployment
kubectl get services
```

If the existing Pod label or application port differs, update `k8s/info-service.yaml` before applying it.

## 1. Run locally (without Kubernetes)

Start your existing Info Service on port 8080 first.

Build and run this project:

```powershell
mvn clean package
mvn spring-boot:run
```

Test the Consumer Service:

```powershell
curl.exe http://localhost:8081/api/consumer
```

Or open `http://localhost:8081/api/consumer` in your browser.

If your existing Info Service is on another host/port, set `INFO_SERVICE_URL` before starting Consumer Service:

```powershell
$env:INFO_SERVICE_URL = "http://localhost:8080"
mvn spring-boot:run
```

## 2. Build the Docker image

Build the JAR:

```powershell
mvn clean package -DskipTests
```

Build the image:

```powershell
docker build -t consumer-service:1.0 .
```

Because you are using Docker Desktop Kubernetes, it can use images built into Docker Desktop's local image store.

## 3. Deploy to Kubernetes

First confirm the existing Info Service Pods have the expected label and port. Then apply the Service that gives them a stable Kubernetes DNS name:

```powershell
kubectl apply -f k8s/info-service.yaml
```

Deploy Consumer Service:

```powershell
kubectl apply -f k8s/consumer-deployment.yaml
kubectl apply -f k8s/consumer-service.yaml
```

Check:

```powershell
kubectl get pods
kubectl get services
kubectl get endpointslices -l kubernetes.io/service-name=info-service
kubectl get endpointslices -l kubernetes.io/service-name=consumer-service
```

The Consumer Service uses this internal Kubernetes URL:

```text
http://info-service:8080/api/info
```

Expose Consumer Service locally through NodePort:

```text
http://localhost:30081/api/consumer
```

If `localhost:30081` is not reachable in your Docker Desktop setup, use port-forwarding:

```powershell
kubectl port-forward service/consumer-service 8081:8081
```

Then open:

```text
http://localhost:8081/api/consumer
```

## 4. Troubleshooting

View Consumer Service logs:

```powershell
kubectl logs deployment/consumer-service
```

Check the Info Service endpoints:

```powershell
kubectl get endpointslices -l kubernetes.io/service-name=info-service -o wide
```

If no Info Service endpoints appear, check that the selector in `k8s/info-service.yaml` matches the labels on your existing Pods. Also confirm both Services and Deployments are in the same namespace.

If the call returns connection refused, check the existing application container port and whether `/api/info` is responding inside the cluster.
