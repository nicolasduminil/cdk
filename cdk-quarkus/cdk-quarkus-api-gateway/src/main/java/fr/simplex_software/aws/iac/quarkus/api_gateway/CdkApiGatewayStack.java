package fr.simplex_software.aws.iac.quarkus.api_gateway;

import jakarta.inject.*;
import org.eclipse.microprofile.config.inject.*;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.s3.*;

@Singleton
public class CdkApiGatewayStack extends Stack
{
  @Inject CdkApiGatewayConfig config;

  public CdkApiGatewayStack(final App scope, final @ConfigProperty(name = "cdk.stack-id", defaultValue = "QuarkusApiGatewayStack") String stackId, final StackProps props)
  {
    super(scope, stackId, props);
  }

  public void initStack()
  {
    Role role = Role.Builder.create(this, config.id() + "-role")
      .assumedBy(new ServicePrincipal("lambda.amazonaws.com")).build();
    role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AmazonS3FullAccess"));
    role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("CloudWatchFullAccess"));
    IFunction function = Function.Builder.create(this, config.id())
      .runtime(Runtime.JAVA_21)
      .role(role)
      .handler(config.handler())
      .memorySize(config.ram())
      .timeout(Duration.seconds(config.timeout()))
      .functionName(config.function())
      .code(Code.fromAsset((String) this.getNode().tryGetContext("zip")))
      .build();
    FunctionUrl functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder().authType(FunctionUrlAuthType.NONE).build());
    new Bucket(this, config.bucketId(), BucketProps.builder().bucketName(config.bucketName()).build());
    CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();
  }
}
