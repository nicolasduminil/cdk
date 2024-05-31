package fr.simplex_software.aws.iac.quarkus_api.tests;

import io.quarkus.test.junit.*;
import jakarta.inject.*;
import org.eclipse.microprofile.config.inject.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@QuarkusTest
public class GetHostResourceIT
{
  @Inject
  @ConfigProperty(defaultValue = "../quarkus-api/target/function.zip", name="cdk-quarkus-api-gateway.zip-location")
  private String zipLocation;

  @Test
  public void test123()
  {
    assertThat(zipLocation).isEqualTo("../quarkus-api/target/function.zip");
  }
}
