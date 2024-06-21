package fr.simplex_software.aws.iac.quarkus.s3.tests;

import fr.simplex_software.aws.iac.quarkus.s3.*;
import io.quarkus.hibernate.validator.runtime.jaxrs.*;
import io.quarkus.test.junit.*;
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

  @Test
  @Order(10)
  public void testUploadFile()
  {
    given()
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
}
