package fr.simplex_software.aws.iac.quarkus.api_gateway;

import jakarta.enterprise.context.*;
import jakarta.enterprise.inject.*;
import jakarta.inject.*;
import software.amazon.awscdk.*;

@ApplicationScoped
public class CdkApiGatewayProducer
{
  @Produces
  @Singleton
  public App app()
  {
    return new App();
  }

  @Produces
  @Singleton
  public StackProps stackProps()
  {
    return StackProps.builder().build();
  }
}
