FROM openjdk:18-jdk-slim

WORKDIR /usr/src/app

COPY target/techstorebe-0.1.0.jar conTicket-0.1.0.jar

ENV DB_PASSWORD=${DB_PASSWORD}
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV JWT_SECRET=${JWT_SECRET}


# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "techstorebe-0.1.0.jar"]