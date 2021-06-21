# Deploying EAR to /tmp via SCP

## EAR Tasks
Please note there are no IBM specific XML files in the EAR nor the WAR. This is on purpose. WAS will generate
default version of the files needed. For the purpose of this project no IBM specific extensions are required.

### How Do I Copy my EAR to /tmp 
### Using maven-antrun-plugin to copy the EAR
This is well documented way to copy the EAR. You will be using nexus or JFrog and this not meant to be a enterprise
solution

```xml
<plugin>
    <artifactId>maven-antrun-plugin</artifactId>
    <version>1.8</version>
    <configuration>
        <target>
            <!--suppress UnresolvedMavenProperty -->
            <echo message="Pushing to @${scp.host} as user : ${scp.user.name}" />
            <!--suppress UnresolvedMavenProperty -->
            <scp todir="${scp.user.name}:${scp.credentials}@${scp.host}:${scp.dir}"
                 port="22"
                 trust="true"
                 failonerror="false"
                 verbose="false">
                <fileset dir="target">
                    <include name="*.ear" />
                </fileset>
            </scp>
```

### Loading Credentials at Runtime for SCP Task
We are als using the *properties-maven-plugin* to load the credentials for the SCP task and not hard code a user name
and password in the project. This is not a WAS specific task. You will need to save the *credentials.properties.template*
file as *credentials.properties* and update the settings. maven will pick those up and replace the values at run time.

```xml
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>properties-maven-plugin</artifactId>
        <version>1.0.0</version>
        <executions>
            <execution>
                <phase>initialize</phase>
                <goals>
                    <goal>read-project-properties</goal>
                </goals>
                <configuration>
                    <files>
                        <file>${basedir}/credentials.properties</file>
                    </files>
                </configuration>
            </execution>
        </executions>
    </plugin>
```





