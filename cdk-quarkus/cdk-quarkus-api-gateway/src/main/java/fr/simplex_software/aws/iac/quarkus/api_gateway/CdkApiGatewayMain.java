package fr.simplex_software.aws.iac.quarkus.api_gateway;

import io.quarkus.runtime.*;
import io.quarkus.runtime.annotations.*;

@QuarkusMain
public class CdkApiGatewayMain
{
  public static void main(String... args)
  {
    Quarkus.run(CdkApiGatewayApp.class, args);
  }
}
