# jSSTL: java Signal Spatio Temporal Logic
jSSTL is a Java library for the specification and verification of spatio-temporal properties of dynamical systems described as spatio-temporal traces over a weighted graph. Given a trace x(t,l), a weighted graph and a formula Phi, it returns the boolean and quantitative spatio-temporal signals of the satisfaction at each time and in each location.

# Installing jSSTL
jSSTL is distributed as a Maven Package. To build jSSTL you have first to install [Apache Maven](https://maven.apache.org/index.html). 

You can download jSSTL source code either by cloning the GitHub repository:

```
git clone https://github.com/Quanticol/jsstl.git
```

This creates a folder (named jsstl) containing all the source code. You can also get the source files in a zip from the following [link](https://github.com/Quanticol/jsstl/archive/master.zip).

Maven can now be used to build and test jSpace. Open a console and enter into the folder ```jsstl``` and type:

```
mvn clean verify
```

After that, Maven will download the required packages, build the framework, and execute all the tests.

Finally, we have to install jSSTL API in the local Maven repository (the precise location of this repository depends on your configuration):

```
mvn install
```

jSSTL is now available and ready to be used in your projects. Some examples of use are available at the following [link](https://github.com/Quanticol/jsstl-examples). 
