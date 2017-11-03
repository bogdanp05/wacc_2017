# 2017_group_16_s3480941_s3540197_s3346935
Project 26: Sentiment analysis

In order to deploy to Google Cloud PLatform:
- Install: docker, docker-compose, gcloud, kubectl, kompose
- Create account on Google Cloud Platform
- Create a project and get key to configure your environment (see guides on how to do it)

IMPORTANT: Copy ./dataset directory to ./backend/target/docker/stage . If the destination dir does not exist, just run 'sbt docker:publishLocal' in the terminal. This will fail but will create the needed directory.

To run the containers locally:
- 'make clear' : deletes all the containers
- 'make local' : build the images and creates the containers

To deploy the containers on GCP:
- 'make undeploy'
- 'make deploy'
- 'kubectl get deployment,svc,pods,pvc' to see the ip address that you need to access

Debugging local:
- docker logs $(CONTAINER_ID)
- docker exec -t -i 5af8c4b35e8b /bin/bash
- docker logs docker exec -t -i 5af8c4b35e8b /bin/bash

Debugging on cloud:
- kubectl get pods --all-namespaces -o=jsonpath='{range .items[*]}{"\n"}{.metadata.name}{":\t"}{range .spec.containers[*]}{.image}{", "}{end}{end}' |\
sort

- kubectl logs backend-3573919465-b9t1
- kubectl exec -t -i backend-3573919465-b9t19 /bin/bash
- 
