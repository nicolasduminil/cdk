package fr.simplex_software.aws.iac.quarkus.api_gateway;

import jakarta.enterprise.context.*;
import jakarta.inject.*;
import org.eclipse.microprofile.config.inject.*;
import software.amazon.awscdk.*;
import software.amazon.awscdk.aws_apigatewayv2_integrations.*;
import software.amazon.awscdk.services.apigatewayv2.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.constructs.*;

@Singleton
public class CdkApiGatewayStack extends Stack
{
  private static final String HANDLER = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
  private static int RAM = 128;
  private static int TIME_OUT = 10;
  private static final String FUNCTION = "QuarkusApiGatewayLambda";
  private static final String ID = "quarkus-api-gateway-lambda";

  public CdkApiGatewayStack(final App scope, final @ConfigProperty(name = "cdk.stack-id", defaultValue = "QuarkusApiGatewayStack") String stackId, final StackProps props)
  {
    super(scope, stackId, props);
    IFunction function = Function.Builder.create(this, ID)
      .runtime(Runtime.JAVA_21)
      .handler(HANDLER)
      .memorySize(RAM)
      .timeout(Duration.seconds(TIME_OUT))
      .functionName(FUNCTION)
      .code(Code.fromAsset((String)this.getNode().tryGetContext("zip")))
      .build();
    FunctionUrl functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder().authType(FunctionUrlAuthType.NONE).build());
    /*String url = HttpApi.Builder.create(this, "HttpApiGatewayIntegration")
      .defaultIntegration(HttpLambdaIntegration.Builder.create("HttpApiGatewayIntegration", function).build()).build().getUrl();*/
    var lambdaIntegration = HttpLambdaIntegration.Builder.create("Toto", function).build();
    var httpApiGateway = HttpApi.Builder.create(this, "Toto")
      .defaultIntegration(lambdaIntegration)
      .build();
    var url = httpApiGateway.getUrl();
    CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();
    CfnOutput.Builder.create(this, "FunctionArnOutput").value(function.getFunctionArn()).build();
    CfnOutput.Builder.create(this, "HttpApiGatewayUrlOutput").value(url).build();
    CfnOutput.Builder.create(this, "HttpApiGatewayCurlOutput").value("curl -i " + url + "/s3").build();
  }
}
