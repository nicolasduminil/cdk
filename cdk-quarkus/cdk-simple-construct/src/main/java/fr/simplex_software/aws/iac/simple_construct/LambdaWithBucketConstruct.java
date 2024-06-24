package fr.simplex_software.aws.iac.simple_construct;

import fr.simplex_software.aws.iac.simple_construct.config.*;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.s3.*;
import software.constructs.*;

public class LambdaWithBucketConstruct extends Construct
{
  private FunctionUrl functionUrl;

  public LambdaWithBucketConstruct(final Construct scope, final String id, LambdaWithBucketConstructConfig config)
  {
    super(scope, id);
    Role role = Role.Builder.create(this, config.functionProps().id() + "-role")
      .assumedBy(new ServicePrincipal("lambda.amazonaws.com")).build();
    role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AmazonS3FullAccess"));
    role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("CloudWatchFullAccess"));
    IFunction function = Function.Builder.create(this, config.functionProps().id())
      .runtime(Runtime.JAVA_21)
      .role(role)
      .handler(config.functionProps().handler())
      .memorySize(config.functionProps().ram())
      .timeout(Duration.seconds(config.functionProps().timeout()))
      .functionName(config.functionProps().function())
      .code(Code.fromAsset((String) this.getNode().tryGetContext("zip")))
      .build();
    functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder().authType(FunctionUrlAuthType.NONE).build());
    new Bucket(this, config.bucketProps().bucketId(), BucketProps.builder().bucketName(config.bucketProps().bucketName()).build());
  }

  public String getFunctionUrl()
  {
    return functionUrl.getUrl();
  }
}
