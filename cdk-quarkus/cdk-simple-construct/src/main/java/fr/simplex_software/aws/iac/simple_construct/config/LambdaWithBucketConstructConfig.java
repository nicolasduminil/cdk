package fr.simplex_software.aws.iac.simple_construct.config;

import io.smallrye.config.*;

@ConfigMapping(prefix = "lambda.with.bucket.construct.config")
public interface LambdaWithBucketConstructConfig
{
  BucketProps bucketProps();
  interface BucketProps
  {
    @WithDefault("my-bucket-8701-id")
    String bucketId();
    @WithDefault("my-bucket-8701")
    String bucketName();
  }
  FunctionProps functionProps();
  interface FunctionProps
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
  }
}
