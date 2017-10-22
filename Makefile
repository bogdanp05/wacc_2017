#type "make cassandra" in terminal to run cassandraDB with docker 
cassandra:
	@docker volume create cassandra-vol
	@docker run --name cassandra -p 7000:7000 -p 9042:9042 -v cassandra-vol:/cassandraDB -d cassandra:3.0

#type "make stop" in terminal to stop the container
stop:
	@docker stop cassandra
	@docker rm cassandra
	@docker rm cassandra-vol