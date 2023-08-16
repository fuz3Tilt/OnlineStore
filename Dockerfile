FROM maven:3.8.5-openjdk-18

RUN mkdir app

WORKDIR app

COPY pom.xml .

RUN mvn verify --fail-never

COPY . .

RUN mvn package -Dmaven.test.skip=true

CMD ["java", "-jar", "target/OnlineStore-2.0.jar"]
