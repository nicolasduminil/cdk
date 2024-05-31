package fr.simplex_software.aws.iac.cdk.starter;

import software.amazon.awscdk.*;

public class CdkStarterApp
{
  public static void main(String ...args)
  {
    App app = new App();
    Tags.of(app).add("project", "The CDK Starter projet");
    Tags.of(app).add("environment", "development");
    Tags.of(app).add("application", "CdkStarterApp");
    new CdkStarterStack(app, "CdkStarterStack", StackProps.builder().build());
    app.synth();
  }
}
