package fr.simplex_software.aws.iac.quarkus.api_gateway;

import io.smallrye.config.*;

@ConfigMapping(prefix = "cdk.app")
interface CdkApiGatewayConfig
{
  @WithDefault("io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest")
  String handler();
  @WithDefault("256")
  int ram();
  @WithDefault("60")
  int timeout();
  @WithDefault("QuarkusApiGatewayLambda")
  String function();
  @WithDefault("quarkus-api-gateway-lambda")
  String id();
  @WithDefault("my-bucket-8701-id")
  String bucketId();
  @WithDefault("my-bucket-8701")
  String bucketName();
}
