provider "github" {
  organization = "olaven"
}

resource "github_repository" "trees" {
  name = "trees"
  description = "A project primarily made to aid exercises in the Enterprise Programming and DevOps courses at Kristiania University College."
  private = false
}

resource "github_branch_protection" "travis" {
  repository = github_repository.trees.name
  branch = "master"
  enforce_admins = true
  
  required_status_checks {
    strict = true
    contexts = ["ci/travis"]
  }
}
