docker rm -f os-test
docker rmi uos:v0.1.0
docker build -t uos:v0.1.0 .