package fr.simplex_software.aws.iac.quarkus_api;

import jakarta.enterprise.context.*;
import jakarta.ws.rs.*;

import java.io.*;
import java.net.*;

import static java.lang.System.Logger.Level.*;

@Path("host")
@ApplicationScoped
public class GetHostResource
{
  private static final String FMT = "*** My IP address is %s";

  @GET
  public String host() throws IOException
  {
    return String.format(FMT, InetAddress.getLocalHost().getHostAddress());
  }
}
