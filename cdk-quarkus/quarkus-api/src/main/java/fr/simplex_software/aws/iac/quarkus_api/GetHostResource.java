package fr.simplex_software.aws.iac.quarkus_api;

import io.quarkus.qute.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

@Path("s3")
@ApplicationScoped
public class GetHostResource
{
  private static final String S3_FMT = "arn:aws:s3:::%s";
  private static final String FMT = "*** My IP address is %s";
  @Inject
  Template s3Info;
  @Inject
  S3Client s3;

  @GET
  public String host() throws IOException
  {
    return String.format(FMT, InetAddress.getLocalHost().getHostAddress());
  }

  @GET
  @Path("info/{bucketName}")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getBucketInfo(@PathParam("bucketName") String bucketName)
  {
    Bucket bucket = s3.listBuckets().buckets().stream().filter(b -> b.name().equals(bucketName)).findFirst().orElseThrow();
    TemplateInstance templateInstance = s3Info.data("bucketName", bucketName, "awsRegionName",
      s3.getBucketLocation(GetBucketLocationRequest.builder().bucket(bucketName).build()).locationConstraintAsString(),
      "arn",  String.format(S3_FMT, bucketName), "creationDate",
      LocalDateTime.ofInstant(bucket.creationDate(), ZoneId.systemDefault()), "versioning",
      s3.getBucketVersioning(GetBucketVersioningRequest.builder().bucket(bucketName).build()));
    List<Tag> tags = new ArrayList<>();
    try
    {
      tags = s3.getBucketTagging(GetBucketTaggingRequest.builder().bucket(bucketName).build()).tagSet();
      if (tags == null || tags.isEmpty())
        tags.add(Tag.builder().key("test").value("test").build());
    }
    catch (S3Exception ex) {}
    templateInstance.data("tags", tags);
    return templateInstance;
  }
}
