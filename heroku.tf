module "olaven-heroku" {
  source = "github.com/olaven/heroku-terraform-module"
  name = "trees"
  buildpacks = [
    "heroku/nodejs",
    "heroku/java"
  ]
}