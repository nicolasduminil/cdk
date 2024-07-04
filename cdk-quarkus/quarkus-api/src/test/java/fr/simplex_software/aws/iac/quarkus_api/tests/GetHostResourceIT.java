package fr.simplex_software.aws.iac.quarkus_api.tests;

import io.quarkus.test.junit.*;
import io.restassured.response.*;
import jakarta.inject.*;
import org.apache.http.*;
import org.eclipse.microprofile.config.inject.*;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@QuarkusTest
public class GetHostResourceIT
{
  @Inject
  @ConfigProperty(defaultValue = "../quarkus-api/target/function.zip", name = "cdk-quarkus-api-gateway.zip-location")
  private String zipLocation;

  @Test
  public void testZipLocation()
  {
    assertThat(zipLocation).isEqualTo("../quarkus-api/target/function.zip");
  }

  @Test
  public void testGetHost()
  {
    Response response = given().when().get("/s3");
    assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.prettyPrint()).contains("*** My IP address is");
  }

  @Test
  public void testTemplate()
  {
    Response response = given().when().get("/s3/info/my-bucket-8701");
    assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    assertThat(response.getBody()).isNotNull();
  }
}
