package fr.simplex_software.aws.iac.quarkus.s3;

import jakarta.inject.*;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.inject.*;
import software.amazon.awssdk.core.*;
import software.amazon.awssdk.core.sync.*;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

import java.util.*;
import java.util.stream.*;

@Path("/s3")
public class S3FileManagementApi
{
  @Inject
  S3Client s3;
  @ConfigProperty(name = "bucket.name")
  String bucketName;

  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(@Valid FileMetadata fileMetadata) throws Exception
  {
    PutObjectRequest request = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(fileMetadata.filename)
      .contentType(fileMetadata.mimetype)
      .build();
    s3.putObject(request, RequestBody.fromFile(fileMetadata.file));
    return Response.ok().status(Response.Status.CREATED).build();
  }

  @GET
  @Path("download/{objectKey}")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response downloadFile(@PathParam("objectKey")String objectKey)
  {
    GetObjectRequest request = GetObjectRequest.builder()
      .bucket(bucketName)
      .key(objectKey)
      .build();
    ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(request);
    Response.ResponseBuilder response = Response.ok(objectBytes.asUtf8String());
    response.header("Content-Disposition", "attachment;filename=" + objectKey);
    response.header("Content-Type", objectBytes.response().contentType());
    return response.build();
  }

  @GET
  @Path("list")
  @Produces(MediaType.APPLICATION_JSON)
  public Response listFiles()
  {
    ListObjectsRequest listRequest = ListObjectsRequest.builder().bucket(bucketName).build();
    return Response.ok(s3.listObjects(listRequest).contents().stream()
      .map(S3File::from)
      .sorted(Comparator.comparing(S3File::getObjectKey))
      .collect(Collectors.toList())).build();
  }
}
