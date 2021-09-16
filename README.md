# AWS Lambda Monitoring Extension

## Use Case
AWS Lambda Monitoring Extension captures Lambda statistics from Amazon CloudWatch and displays them in the AppDynamics Metric Browser.

## Prerequisites

1. Please give the following permissions to the account being used to with the extension.
```
  **cloudwatch:ListMetrics**
  **cloudwatch:GetMetricStatistics**
```
2. Before the extension is installed, the prerequisites mentioned [here](https://community.appdynamics.com/t5/Knowledge-Base/Extensions-Prerequisites-Guide/ta-p/35213) need to be met. Please do not proceed with the extension installation if the specified prerequisites are not met.
3. The extension needs to be able to connect to the AWS Lamda in order to collect and send metrics. To do this, you will have to either establish a remote connection in between the extension and the product, or have an agent on the same machine running the product in order for the extension to collect and send the metrics.

** Note : Running the extension with Machine Agent version 4.5.13 or later.

* If you are seeing warning messages while starting the Machine Agent, update the http-client and http-core JARs in {MACHINE_AGENT_HOME}/monitorsLibs to httpclient-4.5.9 and httpcore-4.4.12 to make this warning go away.                                                                                                            
* To make AWS extensions work on Machine Agent < 4.5.13: 
  The http-client and http-core JARs in {MACHINE_AGENT_HOME}/monitorsLibs has to be manually be updated to httpclient-4.5.9 and httpcore-4.4.12

## Installation
1. Run 'mvn clean install' from aws-lambda-monitoring-extension
2. Copy and unzip AWSLambdaMonitor-\<version\>.zip from 'target' directory into \<machine_agent_dir\>/monitors/
3. Edit config.yml file in AWSLambdaMonitor/conf and provide the required configuration (see Configuration section)
4. Restart the Machine Agent.

Please place the extension in the **"monitors"** directory of your **Machine Agent** installation directory. Do not place the extension in the **"extensions"** directory of your **Machine Agent** installation directory.

## Configuration
In order to use the extension, you need to update the config.yml file that is present in the extension folder. The following is an explanation of the configurable fields that are present in the config.yml file.
All Lambda metrics are available under the namespace AWS/Lambda and provide metrics for a single dimension, the "FunctionName". When retrieving metrics, you must supply both the Value/s for the dimension.

1. If SIM is enabled, then use the following metricPrefix `metricPrefix: "Custom Metrics|AWS Lambda Monitor"` else configure the "COMPONENT_ID" under which the metrics need to be reported.
This can be done by changing the value of <COMPONENT_ID> in `metricPrefix: "Server|Component:<COMPONENT_ID>|Custom Metrics|AWS Lambda Monitor|"`.<br/>
   For example,
     ```
     metricPrefix: "Server|Component:100|Custom Metrics|AWS Lambda Monitor|"
     ```
More details around metric prefix can be found [here](https://community.appdynamics.com/t5/Knowledge-Base/How-do-I-troubleshoot-missing-custom-metrics-or-extensions/ta-p/28695).

2. Provide accessKey(required) and secretKey(required) of AWS account(s), also provide displayAccountName(any name that represents your account) and regions(required).

   ```
   accounts:
     - awsAccessKey: "XXXXXXXX1"
       awsSecretKey: "XXXXXXXXXX1"
       displayAccountName: "TestAccount_1"
       regions: ["us-east-1","us-west-1","us-west-2"]

     - awsAccessKey: "XXXXXXXX2"
       awsSecretKey: "XXXXXXXXXX2"
       displayAccountName: "TestAccount_2"
       regions: ["eu-central-1","eu-west-1"]
   ```
3. If you want to encrypt the "awsAccessKey" and "awsSecretKey" then follow the "Credentials Encryption" section and provide the encrypted values in "awsAccessKey" and "awsSecretKey".
Configure "enableDecryption" of "credentialsDecryptionConfig" to true and provide the encryption key in "encryptionKey"
   For example,
   ```
   #Encryption key for Encrypted password.
   credentialsDecryptionConfig:
       enableDecryption: "true"
       encryptionKey: "XXXXXXXX"
   ```
4. Provide the dimension for the Lambda function you would like to monitor in that dimension.
    ```
    #Filters based on dimensions, values accepts comma separated values and regex patterns. If `.*` is used, all are monitored and if empty, none are monitored
    dimensions:
      - name: "FunctionName"
        displayName: "function-name"
        values: ["eretail-demo", "corpsite-blog"]
    ```

6. Configure the metrics section.

     For configuring the metrics, the following properties can be used:

     |     Property      |   Default value |         Possible values         |                                              Description                                                                                                |
     | :---------------- | :-------------- | :------------------------------ | :------------------------------------------------------------------------------------------------------------- |
     | alias             | metric name     | Any string                      | The substitute name to be used in the metric browser instead of metric name.                                   |
     | statType          | "ave"           | "AVERAGE", "SUM", "MIN", "MAX"  | AWS configured values as returned by API                                                                       |
     | aggregationType   | "AVERAGE"       | "AVERAGE", "SUM", "OBSERVATION" | [Aggregation qualifier](https://docs.appdynamics.com/display/latest/Build+a+Monitoring+Extension+Using+Java)    |
     | timeRollUpType    | "AVERAGE"       | "AVERAGE", "SUM", "CURRENT"     | [Time roll-up qualifier](https://docs.appdynamics.com/display/latest/Build+a+Monitoring+Extension+Using+Java)   |
     | clusterRollUpType | "INDIVIDUAL"    | "INDIVIDUAL", "COLLECTIVE"      | [Cluster roll-up qualifier](https://docs.appdynamics.com/display/latest/Build+a+Monitoring+Extension+Using+Java)|
     | multiplier        | 1               | Any number                      | Value with which the metric needs to be multiplied.                                                            |
     | convert           | null            | Any key value map               | Set of key value pairs that indicates the value to which the metrics need to be transformed. eg: UP:0, DOWN:1  |
     | delta             | false           | true, false                     | If enabled, gives the delta values of metrics instead of actual values.                                        |

    For example,
    ```
      - name: "Invocations"
        alias: "Invocations"
        statType: "ave"
        delta: false
        multiplier: 1
        aggregationType: "AVERAGE"
        timeRollUpType: "AVERAGE"
        clusterRollUpType: "INDIVIDUAL"
    ```

**All these metric properties are optional, and the default value shown in the table is applied to the metric(if a property has not been specified) by default.**

### config.yml

Please avoid using tab (\t) when editing yaml files. Please copy all the contents of the config.yml file and go to [Yaml Validator](https://jsonformatter.org/yaml-validator) . On reaching the website, paste the contents and press the “Validate YAML” button.
If you get a valid output, that means your formatting is correct and you may move on to the next step.

**Below is an example config for monitoring multiple accounts and regions:**

~~~
accounts:
  - awsAccessKey: "XXXXXXXX1"
    awsSecretKey: "XXXXXXXXXX1"
    displayAccountName: "TestAccount_1"
    regions: ["us-east-1","us-west-1","us-west-2"]

  - awsAccessKey: "XXXXXXXX2"
    awsSecretKey: "XXXXXXXXXX2"
    displayAccountName: "TestAccount_2"
    regions: ["eu-central-1","eu-west-1"]

credentialsDecryptionConfig:
    enableDecryption: "false"
    decryptionKey:

proxyConfig:
    host:
    port:
    username:
    password:

#Filters based on dimensions, values accepts comma separated values and regex patterns. If `.*` is used, all are monitored and if empty, none are monitored
dimensions:
  - name: "FunctionName"
    displayName: "function-name"
    values: ["eretail-demo", "corpsite-blog"]

#Allowed values are Basic and Detailed. Refer https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-cloudwatch-new.html for more information
# Basic will fire CloudWatch API calls every 5 minutes
# Detailed will fire CloudWatch API calls every 1 minutes
cloudWatchMonitoring: "Basic"

metricsConfig:
    metricTypes:
      - metricName: "CurrItems"
        statType: "max"

      - metricName: "DecrHits"
        statType: "sum"

    excludeMetrics: ["DeleteMisses", "Get.*"]

    metricsTimeRange:
      startTimeInMinsBeforeNow: 5
      endTimeInMinsBeforeNow: 0

    maxErrorRetrySize: 0

concurrencyConfig:
  noOfAccountThreads: 3
  noOfRegionThreadsPerAccount: 3
  noOfMetricThreadsPerRegion: 3

regionEndPoints:
  us-east-1 : monitoring.us-east-1.amazonaws.com
  us-east-2 : monitoring.us-east-2.amazonaws.com
  us-west-1 : monitoring.us-west-1.amazonaws.com
  us-west-2 : monitoring.us-west-2.amazonaws.com
  ap-northeast-1 : monitoring.ap-northeast-1.amazonaws.com
  ap-northeast-2: monitoring.ap-northeast-2.amazonaws.com
  ap-northeast-3 : monitoring.ap-northeast-3.amazonaws.com
  ap-south-1 : monitoring.ap-south-1.amazonaws.com
  ap-southeast-1 : monitoring.ap-southeast-1.amazonaws.com
  ap-southeast-2 : monitoring.ap-southeast-2.amazonaws.com
  ca-central-1 : monitoring.ca-central-1.amazonaws.com
  cn-north-1 : monitoring.cn-north-1.amazonaws.com.cn
  cn-northwest-1 : monitoring.cn-northwest-1.amazonaws.com.cn
  eu-central-1 : monitoring.eu-central-1.amazonaws.com
  eu-west-1 : monitoring.eu-west-1.amazonaws.com
  eu-west-2 : monitoring.eu-west-2.amazonaws.com
  eu-west-3 : monitoring.eu-west-3.amazonaws.com
  sa-east-1 : monitoring.sa-east-1.amazonaws.com
  us-gov-west-1 : monitoring.us-gov-west-1.amazonaws.com

metricPrefix: "Server|Component:<TIER_ID>|Custom Metrics|AWS Lambda Monitor|"
#metricPrefix: "Custom Metrics|AWS Lambda Monitor|"
~~~

## Metrics
Typical metric path: **Application Infrastructure Performance|\<Tier\>|Custom Metrics|Amazon Lambda|\<Account Name\>|\<Region\>|Function Name|\<Function Name\>** followed by the metrics defined in the link below:
- [Lambda Metrics](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/lam-metricscollected.html)


## Credentials Encryption
Please visit [this page](https://community.appdynamics.com/t5/Knowledge-Base/How-to-use-Password-Encryption-with-Extensions/ta-p/29397) to get detailed instructions on password encryption. The steps in this document will guide you through the whole process.

## Extensions Workbench
Workbench is an inbuilt feature provided with each extension in order to assist you to fine tune the extension setup before you actually deploy it on the controller. Please review the following document on [How to use the Extensions WorkBench](https://community.appdynamics.com/t5/Knowledge-Base/How-to-use-the-Extensions-WorkBench/ta-p/30130)

## Troubleshooting
Please follow the steps listed in this [troubleshooting-document](https://community.appdynamics.com/t5/Knowledge-Base/How-to-troubleshoot-missing-custom-metrics-or-extensions-metrics/ta-p/28695) in order to troubleshoot your issue. These are a set of common issues that customers might have faced during the installation of the extension.

## Contributing
Always feel free to fork and contribute any changes directly here on [GitHub](https://github.com/Appdynamics/aws-lambda-monitoring-extension).

## Version
   |          Name            |  Version   |
   |--------------------------|------------|
   |Extension Version         |2.0.6      |
   |Last Update               |01/06/2021 |
   |Change List               |[ChangeLog](https://github.com/Appdynamics/aws-lambda-monitoring-extension/blob/master/CHANGELOG.md)|
   
**Note**: While extensions are maintained and supported by customers under the open-source licensing model, they interact with agents and Controllers that are subject to [AppDynamics’ maintenance and support policy](https://docs.appdynamics.com/latest/en/product-and-release-announcements/maintenance-support-for-software-versions). Some extensions have been tested with AppDynamics 4.5.13+ artifacts, but you are strongly recommended against using versions that are no longer supported.   
