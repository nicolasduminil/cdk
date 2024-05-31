package fr.simplex_software.aws.iac.simple_construct;

import org.jetbrains.annotations.*;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.s3.*;
import software.constructs.*;

public class LambdaWithBucketBehindApiGateway extends Construct
{
  private static final String FUNCTION_ID = "api-gateway-lambda";
  private static final String BUCKET_ID = "api-gateway-lambda-bucket";

  public LambdaWithBucketBehindApiGateway(final @NotNull Construct scope, @NotNull final String id, final @NotNull BucketProps bucketProps, final @NotNull FunctionProps functionProps)
  {
    super(scope, id);
    @NotNull IBucket bucket = Bucket.fromBucketName(this, BUCKET_ID, bucketProps.getBucketName());
    bucket = new Bucket(this,  BUCKET_ID, bucketProps);
    IFunction function = Function.Builder.create(this, FUNCTION_ID)
      .runtime(functionProps.getRuntime())
      .handler(functionProps.getHandler())
      .memorySize(functionProps.getMemorySize())
      .timeout(functionProps.getTimeout())
      .functionName(functionProps.getFunctionName())
      .code(functionProps.getCode())
      .build();
    FunctionUrl functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder().authType(FunctionUrlAuthType.NONE).build());
    CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();
  }
}
