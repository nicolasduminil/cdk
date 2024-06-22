package fr.simplex_software.aws.iac.quarkus.api_gateway;

import jakarta.enterprise.context.*;
import jakarta.inject.*;
import org.eclipse.microprofile.config.inject.*;
import org.jetbrains.annotations.*;
import software.amazon.awscdk.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.aws_apigatewayv2_integrations.*;
import software.amazon.awscdk.services.apigatewayv2.*;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.s3.*;
import software.constructs.*;

import java.util.*;

@Singleton
public class CdkApiGatewayStack extends Stack
{
  private static final String HANDLER = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
  private static int RAM = 256;
  private static int TIME_OUT = 60;
  private static final String FUNCTION = "QuarkusApiGatewayLambda";
  private static final String ID = "quarkus-api-gateway-lambda";
  private static final String BUCKET_ID = "my-bucket-8701-id";
  private static final String BUCKET_NAME = "my-bucket-8701";

  public CdkApiGatewayStack(final App scope, final @ConfigProperty(name = "cdk.stack-id", defaultValue = "QuarkusApiGatewayStack") String stackId, final StackProps props)
  {
    super(scope, stackId, props);
    /*Role role = Role.Builder.create(this, ID + "-role").roleName(FUNCTION + "Role")
      .assumedBy(ServicePrincipal.Builder.create("lambda.amazonaws.com").build())
      .managedPolicies(List.of(ManagedPolicy.fromAwsManagedPolicyName("AmazonS3FullAccess"))).build();*/
    Role role = Role.Builder.create(this, ID + "-role")
      .assumedBy(new ServicePrincipal("lambda.amazonaws.com")).build();
    role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AmazonS3FullAccess"));
    IFunction function = Function.Builder.create(this, ID)
      .runtime(Runtime.JAVA_21)
      .role(role)
      .handler(HANDLER)
      .memorySize(RAM)
      .timeout(Duration.seconds(TIME_OUT))
      .functionName(FUNCTION)
      .code(Code.fromAsset((String) this.getNode().tryGetContext("zip")))
      .build();

    FunctionUrl functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder().authType(FunctionUrlAuthType.NONE).build());
    //IBucket bucket = Bucket.fromBucketName(this, BUCKET_ID, BUCKET_NAME);
    Bucket bucket = new Bucket(this, BUCKET_ID, BucketProps.builder().bucketName(BUCKET_NAME).build());
    /*String url = HttpApi.Builder.create(this, "HttpApiGatewayIntegration")
      .defaultIntegration(HttpLambdaIntegration.Builder.create("HttpApiGatewayIntegration", function).build()).build().getUrl();*/
    CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();
    /*CfnOutput.Builder.create(this, "BucketARNOutput").value(bucket.getBucketArn());
    CfnOutput.Builder.create(this, "FunctionArnOutput").value(function.getFunctionArn()).build();
    CfnOutput.Builder.create(this, "HttpApiGatewayUrlOutput").value(url).build();
    CfnOutput.Builder.create(this, "HttpApiGatewayCurlOutput").value("curl -i " + url + "/s3").build();
    CfnOutput.Builder.create(this, "FunctionRoleOutput").value(function.getRole().getRoleArn()).build();*/
  }
}
