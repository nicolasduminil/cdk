package fr.simplex_software.aws.iac.cdk.starter.tests;

import com.fasterxml.jackson.databind.*;
import fr.simplex_software.aws.iac.cdk.starter.*;
import org.junit.jupiter.api.*;
import software.amazon.awscdk.*;

import java.io.*;

import static org.assertj.core.api.Assertions.*;

public class TestCdkStarterStack
{
  private final static ObjectMapper JSON = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

  @Test
  public void testCdkStack() throws IOException
  {
    App app = new App();
    Stack cdkStack = new CdkStarterStack(app, "CdkStarterStack", StackProps.builder().build());
    assertThat(JSON.valueToTree(app.synth().getStackArtifact(cdkStack.getArtifactId()).getTemplate()).get("Resources")).isNotNull();
  }
}
