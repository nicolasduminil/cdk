package fr.simplex_software.aws.iac.quarkus.api_gateway;

import fr.simplex_software.aws.iac.simple_construct.*;
import fr.simplex_software.aws.iac.simple_construct.config.*;
import jakarta.inject.*;
import org.eclipse.microprofile.config.inject.*;
import software.amazon.awscdk.*;

@Singleton
public class CdkApiGatewayStack extends Stack
{
  @Inject
  LambdaWithBucketConstructConfig config;
  @ConfigProperty(name = "cdk.lambda-with-bucket-construct-id", defaultValue = "LambdaWithBucketConstructId")
  String lambdaWithBucketConstructId;

  @Inject
  public CdkApiGatewayStack(final App scope,
     final @ConfigProperty(name = "cdk.stack-id", defaultValue = "QuarkusApiGatewayStack") String stackId,
     final StackProps props)
  {
    super(scope, stackId, props);
  }

  public void initStack()
  {
    String functionUrl = new LambdaWithBucketConstruct(this, lambdaWithBucketConstructId, config).getFunctionUrl();
    CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl).build();
  }
}
