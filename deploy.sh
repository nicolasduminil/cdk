#!/bin/bash
if [  $# -ne 2 ]
then
	echo ">>> Usage: $0 [cdk module name (must contain cdk.json file), example: cdk-starter] [api module name (must contain target/functions.zip), example: ../quarkus-api]"
	exit 0
fi
CDK_MODULE_NAME=$1
API_MODULE_NAME=$2
mvn -DskipTests clean install
pushd $API_MODULE_NAME && mvn -DskipITs -Dbase_uri/mp-rest/url=http://localhost:8081 test
popd
pushd $CDK_MODULE_NAME && cdk deploy --all --context zip=~/cdk/$API_MODULE_NAME/target/function.zip --require-approval=never
API_ENDPOINT=$(aws cloudformation describe-stacks --stack-name QuarkusApiGatewayStack --query "Stacks[0].Outputs[?OutputKey=='FunctionURLOutput'].OutputValue" --output text)
#API_ENDPOINT=$(aws cloudformation describe-stacks --stack-name QuarkusApiGatewayStack --query "Stacks[0].Outputs[?OutputKey=='HttpApiGatewayUrlOutput'].OutputValue" --output text)
popd
pushd $API_MODULE_NAME && mvn -Dbase_uri/mp-rest/url=$API_ENDPOINT failsafe:integration-test
popd