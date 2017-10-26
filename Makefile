#type "make cassandra" in terminal to run cassandraDB with docker 
cassandra:
	@docker volume create cassandra-vol
	@docker run --name cassandra -p 7000:7000 -p 9042:9042 -v cassandra-vol:/cassandraDB -d cassandra:3.0

#type "make stop" in terminal to stop the container
stop:
	@docker stop cassandra

start:
	@docker start cassandra

remove:
	@docker rm cassandra
	@docker rm cassandra-vol



# A normal order should be something like: build, push, kompose-up, kompose-down


# Stop and remove all the containers
clear:
	@docker stop $(shell docker ps -aq)
	@docker rm $(shell docker ps -aq)

# Build all the containers
compose:
	#@(cd ./backend && sbt docker:publishLocal)
	@docker-compose up -d --build --remove-orphans

# Build images and tag them
build:
	@docker build -t bogdanp05/frontend:latest ./twitter-analysis-frontend
	@docker build -t bogdanp05/nginx:latest ./nginx
	@(cd ./backend && sbt docker:publishLocal)
	@docker build -t bogdanp05/backend:latest ./backend/target/docker/stage	
	

#Publish the images so that they can be used by kubernetes
push:
	@docker push bogdanp05/frontend  
	@docker push bogdanp05/backend
	@docker push bogdanp05/nginx 

# Deploy to google cloud platform using kubernetes
deploy:
	@kompose up
	@kubectl patch service nginx -p '{"spec":{"type":"LoadBalancer"}}'

undeploy:
	@kompose down

