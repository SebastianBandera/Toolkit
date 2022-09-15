# Toolkit

Toolkit is an experimental system for executing add-in modules.

## Elements

Three projects are found, **ToolkitAPIInterface**, **ToolkitCore** and **ToolkitGUI**.

* **ToolkitAPIInterface** takes care of defining an interface for:
   * Identify the module¨.
   * Define capabilities: runnable, configurable, pauseable, stoppable.
   * Inject dependencies. PathProvider, PausableModuleHelper, ToolkitNotifier and Locker.

* **ToolkitCore** takes care of the core tasks of the application.
    * Provides an implementation of the interfaces of the ToolkitAPIInterface project.
    * Dynamically load installed modules (.jar) that implement the module interface.
    * Create module instances and add defined dependencies to them if needed.

* **ToolkitGUI** is the program interface

![example main window](https://github.com/SebastianBandera/Toolkit/blob/main/images/exampleMain1.PNG?raw=true)


## Build up

You can first make maven resolve the dependencies and install the projects.

About the root of the project (Toolkit)

```console
cd ToolkitAPIInterface
mvn clean install
cd ..
cd ToolkitCore
mvn clean install
cd ..
cd ToolkitGUI
mvn clean install
cd ..
```
Note: if running from powershell on windows, you can use "call" before each maven run to make it work

Running the Toolkit GUI generates the toolkit-files directory with the following structure:
* logs
* main (config and javafx file)
* modules
    * installation
    * storage_files
    * temp_files

In "installation", the generated .jar files must be located.

## How to generate my own module and pack it in a jar?

In a new maven project you can add the dependency to the interface (ToolkitAPIInterface)
```pom
<dependency>
  	<groupId>toolkit</groupId>
  	<artifactId>toolkit.interface</artifactId>
  	<version>0.1-SNAPSHOT[may vary!]</version>
</dependency>
```
Then, you can create your main class of the module, but it must implement toolkit.core.api.module.Module or one or more interfaces from toolkit.core.api.module.capabilities.?

Example
```java
package module.business.tool;

public class MyModule implements RunnableModule, StoppableModule {
...

public String getIdentifier() {
	return "Unique identifier v1.0.0";
}

public String getDescription() {
	return "Awesome description";
}

public void runAction() {
    System.out.println("Hi!");
}

public void requestStop(boolean closingModule) {
	stop = true;
}

public boolean isStopRequested() {
	return stop;
}

...

```

The 
```console
mvn clean install
```
command will generate a .jar that can be placed in the directory 'installation'. If everything is correct, the module will be loaded when starting the application.


## To consider
This work is not considered finished yet. Although it is possible to create and run basic modules.

## License
[GNU General Public License v3.0](https://choosealicense.com/licenses/gpl-3.0/)
