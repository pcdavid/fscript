This is the parent Maven module for the FScript project.

######################################################################
#                        SOURCE ORGANIZATION                         #
######################################################################

The organization of the sources is the following (Maven coordinates
are given between '<' and '>', where 'o.o.f' stands for
'org.objectweb.fractal'):

fscript <o.o.f.fscript:parent> : This directory, containing the
 |     multi-module parent project.
 |
 |- fscript <o.o.f.fscript:fscript> : The actual implementation of
 |     FPath and FScript, as well as the documentation.
 |
 |- fscript-console <o.o.f.fscript:fscript-console> :  A text console
 |     application to interact with FPath and FScript.
 |
 |- fscript-jade <o.o.f.fscript:fscript-jade> : A small extension
 |     to support Jade-specific operations in addition to standard
 |     Fractal operations.

######################################################################
#                            BUILD NOTES                             #
######################################################################

To build FScript and install the produced artifacts in your local
Maven repository, perform the following command:

  $ mvn clean install

FScript depends on the JTA APIs from Sun. Unfortunately, Sun's policy
prevents their redistribution on Maven's main repositories (see
http://maven.apache.org/guides/mini/guide-coping-with-sun-jars.html).
If the build fails because JTA (<javax.transaction:jta:1.1>) is
missing, you must download and install the artifact manually:

- Get the JAR from http://java.sun.com/products/jta/. You want "Class
  Files 1.1" from "Java Transaction API Specification 1.1 Maintenance
  Release".

- Install the resulting file (jta-1_1-classes.zip) into you local
  Maven repository. Perform the following command (on a single line):

    % mvn install:install-file -Dfile=jta-1_1-classes.zip \
                               -DgroupId=javax.transaction \
                               -DartifactId=jta \
                               -Dversion=1.1 \
                               -Dpackaging=jar
