package fr.simplex_software.aws.iac.quarkus.api_gateway;

import software.amazon.awscdk.*;

public class CdkApiGatewayApp
{
  public static void main(String... args)
  {
    App app = new App();
    Tags.of(app).add("project", "API Gateway with Quarkus");
    Tags.of(app).add("environment", "development");
    Tags.of(app).add("application", "CdkApiGatewayApp");
    new CdkApiGatewayStack(app, "QuarkusApiGatewayStack", StackProps.builder().build());
    app.synth();
  }
}
