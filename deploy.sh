#!/bin/sh

# If a command fails then the deploy stops
set -e

printf "\033[0;32mDeploying updates to tangzhongham...\033[0m\n"


# push your files to github
msg="saving file and rebuilding site $(date)"
if [ -n "$*" ]; then
        msg="$*"
fi
git add .
git commit -m "$msg"

# Push source and build repos.
git push origin master

printf "upload success, enjoy your journey! "
