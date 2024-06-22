package fr.simplex_software.aws.iac.quarkus.s3;

import jakarta.annotation.*;
import jakarta.inject.*;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.inject.*;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.*;
import software.amazon.awssdk.core.sync.*;
import software.amazon.awssdk.regions.*;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.*;
import software.amazon.awssdk.core.waiters.*;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

@Path("/s3")
public class S3FileManagementApi
{
  private static System.Logger LOG = System.getLogger(S3FileManagementApi.class.getName());
  private S3Client s3 = S3Client.builder().region(Region.EU_WEST_3).credentialsProvider(DefaultCredentialsProvider.create()).build();
  /*@Inject
  S3Client s3;*/
  @ConfigProperty(name = "bucket.name")
  String bucketName;

  /*@PostConstruct
  public void postConstruct()
  {
    try
    {
      HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
      s3.headBucket(headBucketRequest);
    }
    catch (NoSuchBucketException e)
    {
      CreateBucketRequest bucketRequest = CreateBucketRequest.builder().bucket(bucketName).build();
      s3.createBucket(bucketRequest);
    }
    System.out.println (">>> Have created the bucket " + bucketName);
  }*/

  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(@Valid FileMetadata fileMetadata) throws Exception
  {
    System.out.println("### Uploadfile()");
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
  public Response downloadFile(@PathParam("objectKey") String objectKey)
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
    System.out.println (">>> listFiles()");
    ListObjectsRequest listRequest = ListObjectsRequest.builder().bucket(bucketName).build();
    System.out.println (">>> listFiles(): ListObjectsRequest");
    ListObjectsResponse listObjectsResponse = s3.listObjects(listRequest);
    System.out.println (">>> listFiles(): ListObjectsResponse");
    List<S3Object> s3ObjectList = listObjectsResponse.contents();
    System.out.println (">>> listFiles(): List<S3Objects>");
    List<S3File> s3Files = s3ObjectList.stream().map(S3File::from).sorted(Comparator.comparing(S3File::getObjectKey)).collect(Collectors.toList());
    System.out.println (">>> listFiles(): List<S3File>");
    return Response.ok(s3Files).build();
    /*return Response.ok(s3.listObjects(listRequest).contents().stream()
      .map(S3File::from)
      .sorted(Comparator.comparing(S3File::getObjectKey))
      .collect(Collectors.toList())).build();*/
  }

  @GET
  @Path("time")
  @Produces(MediaType.TEXT_PLAIN)
  public String currentTime()
  {
    System.out.println("### currentTime()");
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu, HH:mm:ss"));
  }

  @GET
  @Path("buckets")
  @Produces(MediaType.APPLICATION_JSON)
  public String getBuckets()
  {
    StringBuffer ret = new StringBuffer();
    ret.append(">>> ");
    //ListBucketsResponse listBuckets = s3.listBuckets(ListBucketsRequest.builder().build());
    ListBucketsResponse response = s3.listBuckets();
    List<Bucket> bucketList = response.buckets();
    for (Bucket bucket: bucketList)
      ret.append(bucket.name() + "; ");
    //s3.listBuckets(ListBucketsRequest.builder().build()).buckets().stream().forEach(b -> ret.append(b + "; "));
    return ret.toString();
  }

  @POST
  @Path("bucket/{name}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String createBucket (@PathParam("name") String name)
  {
    System.out.println("### createBucket(" + name + ")");
    try
    {
      s3.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
    }
    catch (NoSuchBucketException e)
    {
      s3.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
    }
    return "Have created bucket " + name;
  }
}
