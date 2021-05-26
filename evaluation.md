The purpose of vendor folder is to store third party resources, which are used in the project.
The sub-folder structure is used to separate files needed for running the application and test. They can use different libraries, and it is essential to classify them,
basing on the same nature of service. Splitting files is useful because later in build.gradle, 
we can specify "implementation fileTree" and "testImplementation fileTree", 
which shows the program what is needed to compile for one of the tasks.