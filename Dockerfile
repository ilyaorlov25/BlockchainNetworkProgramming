FROM gradle:7.3.3-jdk17 as builder
USER root
WORKDIR /builder
ADD . /builder
RUN ["gradle", "clean", "BlockchainJar"]

FROM openjdk:17-oracle
WORKDIR /blockchainnetworkprogramming
COPY --from=builder /builder/build/libs/BlockchainNetworkProgramming-1.0.jar .
ENTRYPOINT ["java", "-jar", "BlockchainNetworkProgramming-1.0.jar"]