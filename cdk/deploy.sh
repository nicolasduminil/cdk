#!/bin/sh
if [  $# -ne 2 ]
then
	echo ">>> Usage: $0 [cdk module name (must contain cdk.json file), example: cdk-starter] [api module name (must contain target/functions.zip), example: ../quarkus-api]"
	exit 0
fi
CDK_MODULE_NAME=$1
API_MODULE_NAME=$2
mvn clean install
cd $CDK_MODULE_NAME && cdk deploy --all --context zip=$API_MODULE_NAME/target/function.zip --require-approval=never
#cd .. && mvn failsafe:integration-test
