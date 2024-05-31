package fr.simplex_software.aws.iac.cdk.starter;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.s3.*;
import software.constructs.*;

public class CdkStarterStack extends Stack
{
  public CdkStarterStack(final Construct scope, final String id)
  {
    super(scope, id);
  }

  public CdkStarterStack(final Construct scope, final String id, final StackProps props)
  {
    super(scope, id, props);
    Bucket bucket = Bucket.Builder.create(this, "my-bucket-id")
      .bucketName("my-bucket-" + System.getenv("CDK_DEFAULT_ACCOUNT"))
      .autoDeleteObjects(true).removalPolicy(RemovalPolicy.DESTROY).build();
  }
}
