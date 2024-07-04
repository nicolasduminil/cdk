package fr.simplex_software.aws.iac.quarkus_api;

import io.quarkus.qute.*;

import java.time.*;
import java.util.*;

@TemplateData
public class S3BucketInfo
{
  public String bucketName;
  public String awsRegionName;
  public String arn;
  public LocalDateTime creationDate;
  public boolean versioning;
  public List<String> tags = new ArrayList<>();
}
