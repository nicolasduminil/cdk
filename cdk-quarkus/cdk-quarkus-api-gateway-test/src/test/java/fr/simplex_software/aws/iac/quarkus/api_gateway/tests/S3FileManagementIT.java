package fr.simplex_software.aws.iac.quarkus.api_gateway.tests;

import fr.simplex_software.aws.iac.quarkus.s3.*;
import io.quarkus.hibernate.validator.runtime.jaxrs.*;
import io.quarkus.test.junit.*;
import io.restassured.*;
import jakarta.inject.*;
import jakarta.json.*;
import jakarta.ws.rs.core.*;
import org.apache.http.*;
import org.eclipse.microprofile.config.inject.*;
import org.eclipse.microprofile.rest.client.inject.*;
import org.hamcrest.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class S3FileManagementIT
{
  private static final File readme = new File("./src/test/resources/README.md");
  @Inject
  @RestClient
  S3FileManagementClient s3FileManagementTestClient;
  @Inject
  @ConfigProperty(name = "base_uri/mp-rest/url")
  String baseURI;


  /*@Test
  @Order(10)
  public void testUploadFile()
  {
    RestAssured.given()
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .multiPart("file", readme)
      .multiPart("filename", "README.md")
      .multiPart("mimetype", MediaType.TEXT_PLAIN)
      .when()
      .post("/s3/upload")
      .then()
      .statusCode(HttpStatus.SC_CREATED);
  }

  @Test
  @Order(20)
  public void testListFiles()
  {
    RestAssured.given()
      .when().get("/s3/list")
      .then()
      .statusCode(200)
      .body("size()", Matchers.equalTo(1))
      .body("[0].objectKey", Matchers.equalTo("README.md"))
      .body("[0].size", Matchers.greaterThan(0));
  }

  @Test
  @Order(30)
  public void testDownloadFile() throws IOException
  {
    RestAssured.given()
      .pathParam("objectKey", "README.md")
      .when().get("/s3/download/{objectKey}")
      .then()
      .statusCode(200)
      .body(Matchers.equalTo(Files.readString(readme.toPath())));
  }*/

  @Test
  @Order(40)
  public void testUploadFile2() throws Exception
  {
    Response response = s3FileManagementTestClient.uploadFile(new FileMetadata(readme, "README.md", MediaType.TEXT_PLAIN));
    assertThat(response).isNotNull();
    assertThat(response.getStatusInfo().toEnum()).isEqualTo(Response.Status.CREATED);
  }

  @Test
  @Order(50)
  public void testListFiles2()
  {
    Response response = s3FileManagementTestClient.listFiles();
    assertThat(response).isNotNull();
    assertThat(response.getStatusInfo().toEnum()).isEqualTo(Response.Status.OK);
    JsonObject jsonObject = Json.createReader(new StringReader(response.readEntity(String.class))).readArray().getJsonObject(0);
    assertThat(jsonObject.getString("objectKey")).isEqualTo("README.md");
    assertThat(jsonObject.getJsonNumber("size").longValue()).isEqualTo(readme.length());
  }

  @Test
  @Order(60)
  public void testDownloadFile2()
  {
    Response response = s3FileManagementTestClient.downloadFile("README.md");
    assertThat(response).isNotNull();
    assertThat(response.getStatusInfo().toEnum()).isEqualTo(Response.Status.OK);
  }

  @Test
  @Order(70)
  public void testUploadFileShouldFail()
  {
    Assertions.assertThrows(ResteasyReactiveViolationException.class, () ->
      s3FileManagementTestClient.uploadFile(new FileMetadata(null, "README.md", MediaType.TEXT_PLAIN)));
  }

  @Test
  @Order(70)
  public void testUploadFileShouldFail2()
  {
    Assertions.assertThrows(ResteasyReactiveViolationException.class, () ->
      s3FileManagementTestClient.uploadFile(new FileMetadata(readme, "AA", MediaType.TEXT_PLAIN)));
  }

  @Test
  @Order(70)
  public void testUploadFileShouldFail3()
  {
    Assertions.assertThrows(ResteasyReactiveViolationException.class, () ->
      s3FileManagementTestClient.uploadFile(new FileMetadata(readme, "README.md", "aa")));
  }
}
