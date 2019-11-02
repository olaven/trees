#!/usr/bin/env bash

wget https://releases.hashicorp.com/terraform/0.12.10/terraform_0.12.10_linux_amd64.zip
unzip terraform_0.12.10_linux_amd64.zip
chmod +x terraform

./terraform init
./terraform validate
./terraform plan

if [[ $TRAVIS_BRANCH == 'master' ]]
then
    ./terraform apply -auto-approve
fi