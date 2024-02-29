# Utiliser une image de base officielle Java avec JDK 17
FROM openjdk:17-jdk

WORKDIR /graphqletudiant
# Copier les fichiers exécutables .jar dans le conteneur
COPY target/GraphQLEtudiant-0.0.1.jar /graphqletudiant/GraphQLEtudiant.jar
EXPOSE 8080
# Exécuter l'application
CMD java -jar GraphQLEtudiant.jar

