package fr.simplex_software.aws.iac.quarkus.s3;

import software.amazon.awssdk.services.s3.model.*;

public class S3File
{
  private String objectKey;
  private Long size;

  public S3File()
  {
  }

  public String getObjectKey()
  {
    return objectKey;
  }

  public S3File setObjectKey(String objectKey)
  {
    this.objectKey = objectKey;
    return this;
  }


  public Long getSize()
  {
    return size;
  }

  public S3File setSize(Long size)
  {
    this.size = size;
    return this;
  }

  public static S3File from(S3Object s3Object)
  {
    S3File file = new S3File();
    if (s3Object != null)
    {
      file.setObjectKey(s3Object.key());
      file.setSize(s3Object.size());
    }
    return file;
  }
}
