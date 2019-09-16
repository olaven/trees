provider "heroku" {}


## Creating apps

resource "heroku_app" "test" {
  name = "trees-ci"
  region = "eu"
  buildpacks = [
    "heroku/node",
    "heroku/java"
  ]
}

resource "heroku_app" "staging" {
  name = "trees-staging"
  region = "eu"
  buildpacks = [
    "heroku/nodejs",
    "heroku/java"
  ]
}

resource "heroku_app" "production" {
  name = "trees-production"
  region = "eu"
  buildpacks = [
    "heroku/node",
    "heroku/java"
  ]
}


## Creating pipelines

resource "heroku_pipeline" "deploy" {
  name = "trees-deploy"
}

resource "heroku_pipeline_coupling" "staging" {
  app = heroku_app.staging.name
  pipeline = heroku_pipeline.deploy.id
  stage = "staging"
}

resource "heroku_pipeline_coupling" "production" {
  app = heroku_app.production.name
  pipeline = heroku_pipeline.deploy.id
  stage = "production"
}