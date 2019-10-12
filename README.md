# poopy_death
Minecraft mod that adds a poopy death that fits your demise


Setting up and troubleshooting with IntelliJ IDEA or "I SUFFERED, HERE YOU GO":
You will need to run tasks setupDecompWorkspace and genIntellijRuns. Then you might need to edit your run configurations Minecraft Client and Minecraft Server to "use classpath of module": "poopy_death.main".

Once you run it, you might encouter issues with textures missing for custom items, and that means you need to tell IDEA how to build the project. 
Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Build and run using = IntelliJ IDEA. A recent issue with IDEA+Gradle causes the assets directory to not be placed where it belongs.

If you encounter "Cannot start compilation: the output path is not specified for a module", you need to tell IDEA where to place the build: Project Structure -> Project -> Project compiler output = whatever directory you can spare it
