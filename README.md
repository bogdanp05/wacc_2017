# 2017_group_16_s3480941_s3540197_s3346935
Project 26: Sentiment analysis

In order to deploy to Google Cloud PLatform:
- Install: docker, docker-compose, gcloud, kubectl, kompose
- Create account on Google Cloud Platform
- Create a project and get key to configure your environment (see guides on how to do it)


To run the containers locally:
- 'make clear' : deletes all the containers
- 'make local' : build the images and creates the containers

To deploy the containers on GCP:
- 'make undeploy'
- 'make deploy'

Debugging:
- docker exec -t -i 5af8c4b35e8b /bin/bash
- docker logs docker exec -t -i 5af8c4b35e8b /bin/bash
