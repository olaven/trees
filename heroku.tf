module "olaven-heroku" { ## TODO: use this. Not used now, script disabled
  source = "github.com/olaven/heroku-terraform-module"
  name = "trees"
  buildpacks = [
    "heroku/nodejs",
    "heroku/java"
  ]
}