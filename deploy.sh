#!/bin/bash
if [  $# -ne 2 ]
then
	echo ">>> Usage: $0 [cdk module name (must contain cdk.json file), example: cdk-starter] [api module name (must contain target/functions.zip), example: ../quarkus-api]"
	exit 0
fi
CDK_MODULE_NAME=$1
API_MODULE_NAME=$2
mvn -DskipITs -Dfr.simplex_software.aws.iac.quarkus.s3.tests.S3FileManagementTestClient/mp-rest/url=http://localhost:8081 clean install
pushd $CDK_MODULE_NAME && cdk deploy --all --context zip=$API_MODULE_NAME/target/function.zip --require-approval=never
API_ENDPOINT=$(aws cloudformation describe-stacks --stack-name QuarkusApiGatewayStack --query 'Stacks[0].Outputs[0].OutputValue' --output text)
popd
mvn -Dfr.simplex_software.aws.iac.quarkus.s3.tests.S3FileManagementTestClient/mp-rest/url=$API_ENDPOINT failsafe:integration-test
