# AWS Infrastructure as Code with Quarkus

*Infrastructure as Code* (IaC), as the name implies, is a practice which consists in defining infrastructure elements 
with code. This is as opposed to doing it through a GUI (*Graphical User Interface*) like, for example, the AWS Console.
The idea is that, in order to be deterministic and repeatable, the cloud infrastructure must be captured in an abstract 
description, based on models expressed in programming languages, such that to allow the automation of the operations that,
otherwise, should be performed manually.

AWS makes available several IaC tools, as follows:
  - CloudFormation: an IaC tool able to create and manage cloud resources, based on templates expressed in JSON or YAML notation.
  - AWS SDK (*Software Development Kit*): an API (*Application Program Interface*) which provides management support to all AWS services using programming languages like Java, Python, TypeScript and others.
  - AWS CDK (*Cloud Development Kit*): another API like the SDK but more furnished, allowing not only to manage AWS services but also to programmatically create, modify and remove CloudFormation stacks, containing infrastructure elements. It supports many programming languages, including but not limited to Java, Python, TypeScript, etc.

Other IaC tools exist, like Pulumi and Terraform and, even if they aren't developed by AWS, they provide a very interesting 
multi-cloud support. Like AWS SDK and AWS CDK, Pulumi let you define cloud infrastructure using common programming languages
and, like CloudFormation, Terraform uses a dedicated declarative notation, called HCL (*Hashicorp Configuration Language*).

This post is the first part of a series which aims at examining, id depth, all these IaC tools, starting with the CDK.

## Introduction to AWS CDK
In AWS's own definition, CDK is an open source software development framework that defines AWS cloud resources using common
programming languages. Here we'll be using Java.

It's interesting to observe from the beginning that, as opposed to other IaC tools, like CloudFormation or Terraform, the
CDK isn't defined as being just an infrastructure provisioning framework. As a matter of fact, in AWS meaning of the term,
the CDK is more than that: an extremely versatile IaC framework which unleashes the power of programming languages and compilers
to manage highly complex AWS cloud infrastructure with code that is, compared to HCL or any other JSON/YAML based notation, 
much more readable and extensible. As opposed to these other IaC tools, with the CDK one can loop, map, reference, write 
conditions, use helper functions, in a word, take full advantage of the programming languages power.

But the most important advantage of the CDK is that it is a *Domain Specific Language* (DSL), thanks to the extensive 
implementation of the builder design pattern, which allows the developer to easily interact with the AWS services without 
having to learn convoluted APIs and other cloud provisioning syntaxes. Additionally, it makes possible powerful management
and customizations of reusable components, security groups, certificates, load balancers, VPCs (*Virtual Private Cloud*) 
and others.

The CDK is based on the concept on `Construct` as its basic building block. This is a powerful notion  which allows to abstract
away details of common cloud infrastructure patterns. A construct corresponds to one or more synthesized resources, which 
could be a small CloudFormation stack containing just an S3 bucket, or a large one containing a set of EC2 machines with 
the associated AWS Sytem Manager parameter store configuration, security groups, certificates and load balancers. It may be
initialized and reused as many times as required.

The `Stack` is a logical group of `Construct` objetcs. It can be viewed as a chart of the components to be deployed. It 
produces a declarative CloudFormation template, or a Terraform configuration, or a Kubernetes manifest file.

Last but not least, the `App` is a CDK concept which corresponds to a tree of `Construct` objects. There is a root `App` 
which may contain one or more `Stack` objects, containing in turn one or more `Construct` objects, that themselves might 
encompass other `Construct` objects, etc. The figure below depicts this structure.

![img.png](img.png)

There are [here](https://github.com/nicolasduminil/cdk) several examples accompanying this post and illustrating it. They
go from the most simple ones, creating a basic infrastructure, to the most complex ones, dealing with multi-region database
clusters and bastion hosts.



The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation


    ./destroy.sh cdk-quarkus/cdk-quarkus-api-gateway ../cdk-quarkus-s3

Enjoy!
