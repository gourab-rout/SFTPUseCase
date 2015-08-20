#Sftp File Polling

Purpose
=======

Read files from read only Sftp folder.

Components Used To Develop Project
==================================

1. CrushFTP
2. Anypoint Studio
3. mule-ee-distribution-standalone-3.7.1
4. apache-maven-3.3.3-bin

Project setup
==============

### Step 1: Set Up Crush SFTP Server

1. Download crush sftp server from <a href="http://www.crushftp.com/download.html"> http://www.crushftp.com/download.html.
2. Download the zip as per the operating system.
   Extract the zip file and run CRUSHFTP.exe. The following dialogue box is opened
 
    ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/crushftpexe.PNG)

3. Click on "Create New Admin User". Provide the username and password to create the new admin user.
4. Click on "Start Temporary Server" to start sftp. SFTP server starts with the following message

   ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/serverstarted1.PNG)

5.To configure sftp polling path as read only, log on to server <a href="http://127.0.0.1:8080/"> http://127.0.0.1:8080/.
  After log in below page is displayed.

![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/admin.PNG)
  
  Create a new user by navigating "Admin"---> "User Manager" -----> "Add".
  
  ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/usermanager1.png)
  
6.To configure the sftp path click on the newly created user. Drag file from Server to user and give it read only access.

  ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/sftp%20path%20conf1.PNG)

7 Now log on to the sftp server with credentials of newly created user to check if the path has correctly configured.
 
### Step 2: Import Mule project

1. To import the project, first extract "sftp-file-poll.zip". Then import it as "Maven based Mule Project from pom.xml"

    ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/projectimport.PNG)
    
2. The project is developed and tested with studio run time Mule Server 3.6.0 EE. Please ensure mule studio has required munit plugins.
3. Open file sftp.properties available under src/main/resources. Change the properties to reflect actual sftp parameters.
    
![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/sftp cred.PNG)

### Step 3: Code Functinality details

1. By default sftp connector does not handle duplicate file polling. 
2. To handle duplicate file handling scenario, custom java codes are wriiten, which overrides the connector functionality to avoid duplicate file processing. These java files area available under src/main/java/ 
 
  ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/javacode.png)

3. The files which are polled are inserted into database table 'FileDB'. The database used is Mule Embedded in memory derby database. This configuration is done in the class "StorePolledFiles.java". 
4. The class "CustomSftpReceiverRequesterUtil.java" (which checks the available files in inbound folder and picks them for process ) before picking the files to process, checks in the database if the file is already processed.
5. This java class "CustomSftpMessageReceiver.java" (which calls the poll method and "CustomSftpReceiverRequesterUtil.java" ) is injected as the message receiver class for the sftp connector as shown below.

    ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/Capture.PNG)


### Step 4: Running Project in Standalone Server

1. Open command prompt and navigate to project location.
2. Run the command "mvn clean package".
 ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/buildscreen.PNG)
3. When build is successful project archive "sftp-file-poll-1.0.0-SNAPSHOT.zip" is created as shown below
 ![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/target.png)
4. Now copy this archive to appps folder of the standalone server( mule-enterprise-standalone-3.7.1/apps)
5. start mule by navigating to (mule-enterprise-standalone-3.7.1/bin/)
6. When successfully deployed it shows below message 

![ScreenShot]https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/deploy.PNG) 

7. Below is the log snippet
![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/logsnippet.png) 

### Step 5: Test Case

1. In mule end points are by default mocked. The project uses mock sftp data for testing.
2. The project uses java based munit framework to test duplicate file processing scenario.
3. The test java class is available in src/test/java/MunitSFTPPollTest.java
4. When trying to push same file twice below  log snippet is as shown 

![ScreenShot](https://raw.githubusercontent.com/indiramallick1988/Demo2/master/tool/junit.PNG) 
