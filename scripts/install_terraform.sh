curl -sLo /tmp/terraform.zip https://releases.hashicorp.com/terraform/0.12.10/terraform_0.12.10_linux_amd64.zip
unzip /tmp/terraform.zip -d /tmp
mkdir ~/bin
mv /tmp/terraform ~/bin
export PATH="~/bin:$PATH"