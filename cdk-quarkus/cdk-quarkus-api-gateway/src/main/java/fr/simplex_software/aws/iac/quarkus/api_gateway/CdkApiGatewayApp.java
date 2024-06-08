package fr.simplex_software.aws.iac.quarkus.api_gateway;

import io.quarkus.runtime.*;
import io.quarkus.runtime.annotations.*;
import jakarta.enterprise.inject.*;
import software.amazon.awscdk.*;

public class CdkApiGatewayApp implements QuarkusApplication
{
  @Produces
  App app = new App();
  @Produces
  StackProps stackProps = StackProps.builder().build();

  @Override
  public int run(String... args) throws Exception
  {
    Tags.of(app).add("project", "API Gateway with Quarkus");
    Tags.of(app).add("environment", "development");
    Tags.of(app).add("application", "CdkApiGatewayApp");
    app.synth();
    return 0;
  }
}
