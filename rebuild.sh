#!/bin/bash

docker compose down app
docker rmi Referentiel_app:latest
git pull
docker compose up -d app
