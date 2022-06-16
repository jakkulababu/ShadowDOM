## ECAutomation  
Automation suite for the EC it heavily focus on the particular needs of EC, such as:  
 - Shadow root
 - Page object model
 - Multi thread execution
 - Cli execution 
 - BDD tags usage
 - Web driver download
 
 It also provides the same reporting tools as AZFlexFramework 
    
#### Installation
It is very easy to install and setup
```  
git clone url_to_this_repo
cd repo_path
mvn install
```
###### On IDE?
It can be run from eclipse or Intellij, on both  it is import as maven project

#### How to run  
This will run **all** the tests on multiple threads, **10** as default 
```  
mvn integration-test  
```
if you just want to run a single one, it has to have a BDD tag 

    mvn integration-test -D tags=my_tag_of_the_scenario

However if you have multiple scenarios with the same tag, it will execute all of them
###### On IDE?
There is a nice menu on IntelliJ check:
https://stackoverflow.com/a/51545462/774719 

#### Debug
On Intellij add this option:

    mvn integration-test -DforkCount=0