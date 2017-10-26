# 2017_group_16_s3480941_s3540197_s3346935
Project 26: Sentiment analysis

In order to deploy to Google Cloud PLatform:
- Install: docker, docker-compose, gcloud, kubectl, kompose
- Create account on Google Cloud Platform
- Create a project and get key to configure your environment (see guides on how to do it)
- 'make build' : create images for backend, frontend, nginx 
- 'make push' : publish these private images to docker hub
- 'make deploy' : deploy to GCP
- 'make undeploy' : undeploy

