#!/bin/sh

docker run -d \
    --network host \
    --name postgres \
    --env POSTGRES_DB=worklifeintegration \
    --env POSTGRES_USER=postgres \
    --env POSTGRES_HOST_AUTH_METHOD=trust \
    postgres:latest
