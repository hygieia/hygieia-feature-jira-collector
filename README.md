## Hygieia collector for collecting Features from Jira

[![Build Status](https://travis-ci.com/Hygieia/hygieia-testresults-jiraxray-collector.svg?branch=master)](https://travis-ci.com/Hygieia/hygieia-testresults-jiraxray-collector)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Hygieia_hygieia-feature-jira-collector&metric=alert_status)](https://sonarcloud.io/dashboard?id=Hygieia_hygieia-feature-jira-collector)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/Hygieia/hygieia-feature-jira-collector.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Hygieia/hygieia-feature-jira-collector/alerts/)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Hygieia/hygieia-feature-jira-collector.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Hygieia/hygieia-feature-jira-collector/context:java)
[![Maven Central](https://img.shields.io/maven-central/v/com.capitalone.dashboard/jira-feature-collector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.capitalone.dashboard%22%20AND%20a:%22jira-feature-collector%22)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://www.apache.org/licenses/LICENSE-2.0)
<br>
<br>

Configure the JIRA Collector to display and monitor information (related to features/issues) on the Hygieia Dashboard, from JIRA issue boards. This collector retrieves feature content data from the source system APIs and places it in MongoDB for later retrieval and use by the DevOps Dashboard.

Hygieia uses Spring Boot to package the collector as an executable JAR file with dependencies.

# Table of Contents
* [Setup Instructions](#setup-instructions)
* [Sample Application Properties](#sample-application-properties)
* [Troubleshooting](#troubleshooting)
* [Run collector with Docker](#run-collector-with-docker)

## Setup Instructions

To configure the Jira Collector, execute the following steps: 

*	**Step 1 - Artifact Preparation:**

	Please review the two options in Step 1 to find the best fit for you. 

	***Option 1 - Download the artifact:***

	You can download the SNAPSHOTs from the SNAPSHOT directory [here](https://oss.sonatype.org/content/repositories/snapshots/com/capitalone/dashboard/jira-feature-collector/) or from the maven central repository [here](https://search.maven.org/artifact/com.capitalone.dashboard/jira-feature-collector).  

	***Option 2 - Build locally:***

	To configure the Jira Collector, git clone the [jira collector repo](https://github.com/Hygieia/hygieia-feature-jira-collector).  Then, execute the following steps:

	To package the jira collector source code into an executable JAR file, run the maven build from the `\hygieia-feature-jira-collector` directory of your source code installation:

	```bash
	mvn install
	```

	The output file `[collector name].jar` is generated in the `hygieia-feature-jira-collector\target` folder.

	Once you have chosen an option in Step 1, please proceed: 

*	**Step 2: Set Parameters in the Application Properties File**

	Set the configurable parameters in the `application.properties` file to connect to the Dashboard MongoDB database instance, including properties required by the Sonar Collector. To configure the parameters, refer to the [application properties](#sample-application-properties) section.

	For more information about the server configuration, see the Spring Boot [documentation](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files).

*	**Step 3: Deploy the Executable File**

	To run the [collector name].jar file, change directory to 'hygieia-feature-jira-collector\target' and then execute the following command from the command prompt:

	```bash
	java -jar [collector name].jar --spring.config.name=feature --spring.config.location=[path to application.properties file]
	```

### Sample Application Properties

The sample `application.properties` file (with minimum overrides) lists parameters with sample values to configure the JIRA Collector. Set the parameters based on your environment setup.

```properties
# Database Name
dbname=dashboarddb

# Database HostName - default is localhost
dbhost=localhost

# Database Port - default is 27017
dbport=27017

# MongoDB replicaset
dbreplicaset=[false if you are not using MongoDB replicaset]
dbhostport=[host1:port1,host2:port2,host3:port3]

# Database Username - default is blank
dbusername=dashboarduser

# Database Password - default is blank
dbpassword=dbpassword

# Logging File location
logging.file=./logs/jira.log

# PageSize - Expand contract this value depending on Jira implementation's
# default server timeout setting (You will likely receive a SocketTimeoutException)
feature.pageSize=100

# Delta change date that modulates the collector item task
# Occasionally, these values should be modified if database size is a concern
feature.deltaStartDate=2016-03-01T00:00:00.000000
feature.masterStartDate=2016-03-01T00:00:00.000000
feature.deltaCollectorItemStartDate=2016-03-01T00:00:00.000000

# Chron schedule: S M D M Y [Day of the Week]
feature.cron=0 * * * * *

# ST Query File Details - Required, but DO NOT MODIFY
feature.queryFolder=jiraapi-queries
feature.storyQuery=story
feature.epicQuery=epic

# JIRA CONNECTION DETAILS:
# Enterprise Proxy - ONLY INCLUDE IF YOU HAVE A PROXY
feature.jiraProxyUrl=http://proxy.com
feature.jiraProxyPort=9000
feature.jiraBaseUrl=https://jira.com/
feature.jiraQueryEndpoint=rest/api/2/
# For basic authentication, requires username:password as string in base64
# This command will make this for you:  echo -n username:password | base64

feature.jiraCredentials=dXNlcm5hbWU6cGFzc3dvcmQ=

# OAuth is not fully implemented; please blank-out the OAuth values:

feature.jiraOauthAuthtoken=
feature.jiraOauthRefreshtoken=
feature.jiraOauthRedirecturi=
feature.jiraOauthExpiretime=

#############################################################################
# In Jira, general IssueType IDs are associated to various 'issue'
# attributes. However, there is one attribute which this collector's
# queries rely on that change between different instantiations of Jira.
# Please provide a string name reference to your instance's IssueType for
# the lowest level of Issues (for example, 'user story') specific to your Jira
# instance.  Note:  You can retrieve your instance's IssueType Name
# listings via the following URI:  https://[your-jira-domain-name]/rest/api/2/issuetype/
# Multiple comma-separated values can be specified.
#############################################################################
feature.jiraIssueTypeNames=Story

#############################################################################
# In Jira, your instance will have its own custom field created for 'sprint' or 'timebox' details,
# which includes a list of information.  This field allows you to specify that data field for your
# instance of Jira. Note: You can retrieve your instance's sprint data field name
# via the following URI, and look for a package name com.atlassian.greenhopper.service.sprint.Sprint;
# your custom field name describes the values in this field:
# https://[your-jira-domain-name]/rest/api/2/issue/[some-issue-name]
#############################################################################
feature.jiraSprintDataFieldName=customfield_10000

#############################################################################
# In Jira, your instance will have its own custom field created for 'super story' or 'epic' back-end ID,
# which includes a list of information.  This field allows you to specify that data field for your instance
# of Jira.  Note:  You can retrieve your instance's epic ID field name via the following URI where your
# queried user story issue has a super issue (for example, epic) tied to it; your custom field name describes the
# epic value you expect to see, and is the only field that does this for a given issue:
# https://[your-jira-domain-name]/rest/api/2/issue/[some-issue-name]
#############################################################################
feature.jiraEpicIdFieldName=customfield_10002

#############################################################################
# In Jira, your instance will have its own custom field created for 'story points'
# This field allows you to specify that data field for your instance
# of Jira.  Note:  You can retrieve your instance's storypoints ID field name via the following URI where your
# queried user story issue has story points set on it; your custom field name describes the
# story points value you expect to see:
# https://[your-jira-domain-name]/rest/api/2/issue/[some-issue-name]
#############################################################################
feature.jiraStoryPointsFieldName=customfield_10003

#############################################################################
# In Jira, your instance will have its own custom field created for 'team'
# This field allows you to specify that data field for your instance
# of Jira.  Note:  You can retrieve your instance's team ID field name via the following URI where your
# queried user story issue has team set on it; your custom field name describes the
# team value you expect to see:
# https://[your-jira-domain-name]/rest/api/2/issue/[some-issue-name]
#############################################################################
feature.jiraTeamFieldName=

# Defines how to update features per board. If true then only update based on enabled collectorItems otherwise full update
feature.collectorItemOnlyUpdate=true

#Defines the maximum number of features allow per board. If limit is reach collection will not happen for given board
feature.maxNumberOfFeaturesPerBoard=1000

# Set this to true if you use boards as team
feature.jiraBoardAsTeam=false

#Defines the number of hours between each board/team and project data refresh
feature.refreshTeamAndProjectHours=3
```

## Troubleshooting

#### The JIRA collector log does not pull data in for XXXX

Verify the JIRA collector configuration for the custom fields is setup correctly. Hit the rest API outlined in the sample application properties above to see what data is being pulled in. A healthy log will look something like this:
```
2016-09-01 07:27:00,006 INFO c.c.d.collector.CollectorTask - Running Collector: Jira
2016-09-01 07:27:00,010 INFO c.c.d.collector.CollectorTask - -----------------------------------
2016-09-01 07:27:00,011 INFO c.c.d.collector.CollectorTask - https://my.jira.com/
2016-09-01 07:27:00,011 INFO c.c.d.collector.CollectorTask - -----------------------------------
2016-09-01 07:27:02,571 INFO c.c.d.collector.CollectorTask - Team Data 15 2s
2016-09-01 07:27:03,050 INFO c.c.d.collector.CollectorTask - Project Data 15 1s
2016-09-01 07:27:03,752 INFO c.c.d.collector.CollectorTask - Story Data 36 1s
2016-09-01 07:27:03,752 INFO c.c.d.collector.CollectorTask - Finished 4s
```

#### My JIRA widget dropdown does not show any teams.

- Verify your API container is configured to hit the correct database.
- Connect to the Mongo database using a tool such as RoboMongo and check that the 'feature' or 'team' collection (table) has data.
- Check the JIRA URL to see if you are getting any team data in response. Example JIRA URL: https://your-jira-base-url/rest/agile/1.0/board?startAt=0.
- When you run your JIRA collector, check if there are any exceptions in the log.
- Before running the JIRA collector, refer to the [Sample Application Properties](#sample-application-properties-file) file for a listing of the jira collector properties. If your JIRA instance uses Boards as Teams (instead of the Teams plugin), then set ```feature.jiraBoardAsTeam``` property to ```true```.


#### My JIRA widget shows all 0's for estimates

Verify your JIRA collector configuration. Verify that the JIRA collector is pulling in data by observing the logs. Connect to the mongo database using a tool such as RoboMongo and check that the 'feature' collection has data. Check that features associated to an active sprint have the sEstimate (sEstimateTime for hours) field populated.

#### My JIRA widget only shows Kanban sprints

To show scrum sprints, there must exist stories with sprints attached to them that are active and have a recent start date. You can verify that this information is being pulled by either hitting the rest API or checking the mongo database in the feature collection.

#### ERROR c.c.d.client.DefaultJiraClient - No result was available from JIRA unexpectedly - defaulting to blank response. The reason for this fault is the following:RestClientException{statusCode=Optional.of(403), errorCollections=[]}

This may happen if you have had too many failed login attempts and a CAPTCHA guard has been triggered. Try logging in to JIRA with a browser successfully to remove the CAPTCHA guard. Verify that the JIRA credentials are correct.

#### My issue is not listed or has not been resolved

Search active and closed issues on GitHub for 'jira'. Chances are your configuration is wrong and someone else has struggled through fixing it in another issue. Please refrain from commenting on closed issues. Github link: https://github.com/capitalone/Hygieia/issues?q=jira

#### In the JIRA widget, the data for features in Done and WIP status exceeds the actual number in the project (where Sprint Type is ‘Kanban’).

For a Kanban board, the API pulls all the issues that match the following criteria:

- The feature does not have a sprint set
- The feature has a sprint set without an end date
- The feature has a sprint set with end date >= EOT (9999-12-31T59:59:59.999999)

## Run collector with Docker

You can install Hygieia by using a docker image from docker hub. This section gives detailed instructions on how to download and run with Docker. 

*	**Step 1: Download**

	Navigate to the docker hub location of your collector [here](https://hub.docker.com/u/hygieiadoc) and download the latest image (most recent version is preferred).  Tags can also be used, if needed.

*	**Step 2: Run with Docker**

	```Docker run -e SKIP_PROPERTIES_BUILDER=true -v properties_location:/hygieia/config image_name```
	
	- <code>-e SKIP_PROPERTIES_BUILDER=true</code>  <br />
	indicates whether you want to supply a properties file for the java application. If false/omitted, the script will build a properties file with default values
	- <code>-v properties_location:/hygieia/config</code> <br />
	if you want to use your own properties file that located outside of docker container, supply the path here. 
		- Example: <code>-v /Home/User/Document/application.properties:/hygieia/config</code>
