package fr.simplex_software.aws.iac.quarkus.s3.tests;

import fr.simplex_software.aws.iac.quarkus.s3.*;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.rest.client.inject.*;

@Path("s3")
@RegisterRestClient/*(configKey = "base_uri")*/
public interface S3FileManagementTestClient
{
  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(@Valid FileMetadata fileMetadata) throws Exception;
  @GET
  @Path("download/{objectKey}")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response downloadFile(@Valid @PathParam("objectKey")String objectKey);
  @GET
  @Path("list")
  @Produces(MediaType.APPLICATION_JSON)
  public Response listFiles();
}
