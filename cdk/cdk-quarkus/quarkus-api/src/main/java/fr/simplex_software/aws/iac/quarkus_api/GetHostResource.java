package fr.simplex_software.aws.iac.quarkus_api;

import jakarta.enterprise.context.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.*;
import java.net.*;

@Path("host")
@ApplicationScoped
public class GetHostResource
{
  private static final String FMT = "*** My IP address is %s";

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String host() throws IOException
  {
    return String.format(FMT, InetAddress.getLocalHost().getHostAddress());
  }
}
