# 多阶段构建：Maven 构建 + JRE 运行
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B -q
COPY src ./src
RUN mvn package -DskipTests -B -q

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# 限制 JVM 内存占用（小内存服务器友好）
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=60", "-Duser.timezone=Asia/Shanghai", "-jar", "app.jar"]
