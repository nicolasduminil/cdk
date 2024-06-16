#!/bin/bash
if [  $# -ne 2 ]
then
	echo ">>> Usage: $0 [cdk module name (must contain cdk.json file), example: cdk-starter] [api module name (must contain target/functions.zip), example: ../quarkus-api]"
	exit 0
fi
pushd $1 && cdk destroy --context zip=~/cdk/$2/target/function.zip