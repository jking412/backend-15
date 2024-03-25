docker rm -f os-test
docker run --name os-test -d -it -p 6080:6080 kylin:latest /bin/bash
docker exec -it os-test /bin/bash
