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


# Stop and remove all the containers
clear:
	@docker stop $(shell docker ps -aq)
	@docker rm $(shell docker ps -aq)

# Build all the containers
compose:
	@(cd ./backend && sbt docker:publishLocal)
	@docker-compose up -d --build --remove-orphans
