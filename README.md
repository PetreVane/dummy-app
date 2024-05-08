

# Dummy Vaadin EC2 Info Application

This project is a dummy Vaadin application designed to run on an AWS EC2 instance. 

This dummy app is used when testing infrastructure code via terraform. More precisely,
this app jar is stored on a s3 bucket. A launch template contains a bash script, which gets the jar and starts the application.

See [AWS Auto Scaling Infrastructure](https://github.com/PetreVane/tf-autoscaling-infrastructure) for more details.

It displays the public IP address and the availability zone of the EC2 instance hosting the application, by making a asynchronous call to aws metadata endpoints. See `services/Ec2Info.java` and `views/Ec2infoView.java` if you're curious.

This is not useful for anything else other than testing.

## Running the Application

The project is a standard Maven project. To run it from the command line:
- Type `mvnw` (Windows), or `./mvnw` (Mac & Linux).
- Open http://localhost:8080 in your browser.

You can also import the project into your IDE of choice as you would with any Maven project. For guidance on importing Vaadin projects into different IDEs (Eclipse, IntelliJ IDEA, NetBeans, and VS Code), visit [Vaadin's Step-by-Step Guide](https://vaadin.com/docs/latest/guide/step-by-step/importing).

## Deploying to Production

To create a production build, execute:
- `mvnw clean package -Pproduction` (Windows), or
- `./mvnw clean package -Pproduction` (Mac & Linux).

This command builds a JAR file including all dependencies and front-end resources, which is then ready for deployment. Find the JAR in the `target` folder after the build completes.

To run the production build, use:
- `java -jar target/my-app-1.0-SNAPSHOT.jar`

## Application Overview

- **MainLayout.java**: Located in `src/main/java`, contains the navigation setup (e.g., the side/top bar and the main menu) using [Vaadin's App Layout](https://vaadin.com/docs/components/app-layout).
- **views**: This package in `src/main/java` contains the server-side Java views of your application. In particular, `Ec2InfoView` fetches and displays EC2 metadata.
- **frontend/views**: Contains client-side JavaScript views of your application.
- **frontend/themes**: Contains custom CSS styles.

## Key Features

- **Real-time EC2 Info**: Displays real-time information about the EC2 instance it is run on, including IP address and availability zone (when it works properly, which is not always the case 😐).
