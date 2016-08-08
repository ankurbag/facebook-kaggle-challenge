# Facebook_Kaggle_Challenge
A Big Data project


This repository contains two sub-projects and one R-code analysis:
## Technologies Used 
1. Web service created using Java-JSP-Servlet
2. Database - MongoDB
3. IDE used - Eclipse NEON
4. Server used - Tomcat  / Spark
6. AWS
5. R


##1.fbcheckin_ui
This is the interface for the user who inserts his location as (x,y) coordinate and sees which location is predicted for him

###Steps to run
1. Download the git repository on your local
2. Import as Maven Web Project
3. Set up Tomcat in the servers
http://localhost:8080/fbcheckin/

##2.Hadoop_Projects_0_20
This is the Hadoop 0.20 projects to implement the Supervised Learning on training data

###Steps to run
1. Download the git repository on your local
2. Import as General Project
3. Run the following java classes:
![image](https://cloud.githubusercontent.com/assets/3647390/17494736/0995c162-5d84-11e6-8c8b-a8cda814b729.PNG)
![image](https://cloud.githubusercontent.com/assets/3647390/17494733/0981d904-5d84-11e6-968a-09f39af59dd6.PNG)
You will see series of trained outputs
3. Running Location Generator - It is a map reduce job which parallely create location blocks
![image](https://cloud.githubusercontent.com/assets/3647390/17494730/0980c208-5d84-11e6-887e-1fc8184818fc.PNG)
![image](https://cloud.githubusercontent.com/assets/3647390/17494732/0981df94-5d84-11e6-840a-71302482d9eb.PNG)
4. Running DB insertion job - Map Reduce job to insert into MongoDB schema
![image](https://cloud.githubusercontent.com/assets/3647390/17494731/09819a7a-5d84-11e6-95fc-b302f95e6ace.PNG)
![image](https://cloud.githubusercontent.com/assets/3647390/17494729/097a3b90-5d84-11e6-8ba4-677add93d1a6.PNG)
5. Running KNN - It is the validation / testing phase
![image](https://cloud.githubusercontent.com/assets/3647390/17494734/09837b74-5d84-11e6-8c32-e9e6cf299bfb.PNG)
![image](https://cloud.githubusercontent.com/assets/3647390/17494735/0988e4c4-5d84-11e6-9170-f64e19b90a2a.PNG)

##3.R-code
1. HadoopProjectAnalysis : For analysis of data
2. Running K-means Training Algorithm
