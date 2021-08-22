docker build --platform linux/amd64 ./docker-backend -t benarmstrong/sepm-backend:latest
docker push benarmstrong/sepm-backend:latest

docker build --platform linux/amd64 ./docker-frontend -t benarmstrong/sepm-client:latest
docker push benarmstrong/sepm-client:latest

docker build --platform linux/amd64 ./proxy -t benarmstrong/sepm-proxy:latest
docker push benarmstrong/sepm-proxy:latest