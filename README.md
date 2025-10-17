## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Upgrading to Java 21 (LTS)

This project can be built with Java 21 (current LTS). Below are recommended steps to install a JDK 21 on Windows, verify the installation, and configure the project in Visual Studio Code.

1) Install a JDK 21 distribution (examples):

- Using winget (Windows Package Manager) — open PowerShell as Administrator and run one of the following (choose the provider you prefer):

	# Use Eclipse Temurin (Adoptium)
	winget install --id EclipseAdoptium.Temurin.21 -e

	# Or use Microsoft Build of OpenJDK 21
	winget install --id Microsoft.OpenJDK.21 -e

	If you prefer Chocolatey, use `choco install temurin21jdk` (or the package id you trust).

2) Set JAVA_HOME and update PATH (PowerShell example):

	$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21'  # adjust path to where your JDK installed
	[Environment]::SetEnvironmentVariable('JAVA_HOME', $env:JAVA_HOME, 'Machine')
	$oldPath = [Environment]::GetEnvironmentVariable('Path', 'Machine')
	if (-not $oldPath.Contains("%JAVA_HOME%")) {
			[Environment]::SetEnvironmentVariable('Path', "$oldPath;${env:JAVA_HOME}\bin", 'Machine')
	}

3) Verify the installation in a new PowerShell session:

	java -version
	javac -version

Both should report version 21 (for example: openjdk version "21.0.1" 2024-09-17).

4) Project configuration options

- Gradle: A `build.gradle` is provided in the project root that configures the Gradle Java toolchain to target Java 21. If you use Gradle, run `gradle build` (or generate/refresh the Gradle wrapper).
- VS Code: You can point VS Code to your JDK 21 by creating (or editing) `.vscode/settings.json` and adding an entry under `java.configuration.runtimes` with the actual installation path. An example file is included in this repository.

Notes:
- You may need to restart VS Code after installing a new JDK so the Java extension picks up the new runtime.
- If you're using an IDE (IntelliJ/Eclipse) configure the project's SDK/JDK to use Java 21.
