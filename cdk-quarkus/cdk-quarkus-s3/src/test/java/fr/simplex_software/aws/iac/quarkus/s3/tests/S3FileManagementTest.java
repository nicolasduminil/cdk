package fr.simplex_software.aws.iac.quarkus.s3.tests;

import fr.simplex_software.aws.iac.quarkus.s3.*;
import io.quarkus.hibernate.validator.runtime.jaxrs.*;
import io.quarkus.test.junit.*;
import io.restassured.http.Header;
import jakarta.inject.*;
import jakarta.json.*;
import jakarta.ws.rs.core.*;
import org.apache.http.*;
import org.eclipse.microprofile.config.inject.*;
import org.eclipse.microprofile.rest.client.inject.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class S3FileManagementTest
{
  private static File readme = new File("./src/test/resources/README.md");
  @Inject
  @RestClient
  S3FileManagementTestClient s3FileManagementTestClient;

  @Test
  @Order(10)
  public void testUploadFile()
  {
    given()
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .header(new Header("content-type", MediaType.MULTIPART_FORM_DATA))
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
    given()
      .when().get("/s3/list")
      .then()
      .statusCode(200)
      .body("size()", equalTo(1))
      .body("[0].objectKey", equalTo("README.md"))
      .body("[0].size", greaterThan(0));
  }

  @Test
  @Order(30)
  public void testDownloadFile() throws IOException
  {
    given()
      .pathParam("objectKey", "README.md")
      .when().get("/s3/download/{objectKey}")
      .then()
      .statusCode(200)
      .body(equalTo(Files.readString(readme.toPath())));
  }

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
