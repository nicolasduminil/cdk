package fr.simplex_software.aws.iac.quarkus.s3;

import jakarta.ws.rs.core.*;
import org.jboss.resteasy.reactive.*;

import java.io.*;

public class FileMetadata
{
  @RestForm
  public File file;

  @RestForm
  @PartType(MediaType.TEXT_PLAIN)
  /*@NotEmpty
  @Size(min = 3, max = 40)*/
  public String filename;

  @RestForm
  @PartType(MediaType.TEXT_PLAIN)
  /*@NotEmpty
  @Size(min = 10, max = 127)*/
  public String mimetype;

  public FileMetadata()
  {
  }

  public FileMetadata(File file, String filename, String mimetype)
  {
    this.file = file;
    this.filename = filename;
    this.mimetype = mimetype;
  }
}
