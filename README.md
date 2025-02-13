# ♕ BYU CS 240 Chess

Server design website
[serverdesign](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5xDAaTgALdvYoALIoAIye-Ezw-g7BKABMmPZQEACu2DAAxGjAVJYwAEoo9kiqFnJIEGhZAO6+SGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9KkGUAA6aADeAETzlDkAtihrvWswawA0R7jqNdAc+4cnRyjbwEgIN0cAvpjCPTAdrOxclH6602UB2ewOR1Oa3OqkuUGuELuaweTxeiI+bE43Fgvy+on6UGKpUaUAAFEUSmVKEUAI6pNRgACUn26oh+nVk8iUKnU-UCYAAqgtSSCwczOYplGpVOyjDpegAxJCcGBCygSmA6fKi4C7TAS7nSn641kqfpoVIIBAskQqWUGqW8kCEuQoNVkkES8XaQ3qWXGXoKDgcVULCU2qhs34OnmqXrOlCuhSpMC+UnAFO+L36n2OmW-ANBkPJ1PhvF21q-TEAvqFIlUqBFVSWrDV7HGzpfQEwYELMGHfq3KEZ1PjCAAa3QA6O7wjlA78GQ5n68ScTmWva2ur2MEHkKOI98Y8naGntw+6A4pmSaQymWgAUMABkICVqpk6g0mku2p3ut3hjGKZZgMdRKjQDcdV2U4YThDgPi7HFOjbbsViOKDwTWJFYKuG5Dgxf52xNW0UH6BBX2VUkXzfWl6TKZly0MaNc1jPkUEFYUMO9Lk839eUYCVFV3Q1LUYAwnMeNjBdGP6EEZS0eQIyjDkWOlfoEyTTN00zbMYyNAt+OAYMYEPMtTSYqtCO7ajlSbFtMBQnFiJ6IF0L7bc8OHTNjynA58LnJzOmaZcYFXddVjcrddk8g9vInXysJgD4zE4a8UnSLJkhQdAYGotJmA-epGkwYLfy6agAOkABRJ8qvGKrphmUDVHA5ZDx8tAAtlRz+na+LOsc2UZJgcj7HyqjX3y2iGQY8z7VU3kYH5Es0z6k9uMlKSDP6AAzZVix07QYBqBpfBMuKTwkzb9Oc-EYBWsySPmyS1KMFBuEaFbtNHfqNt9fNOgDN6PrUe7MwU4BjtO86fsuxiFx63LJpTOyEFbKzAvKlyewQ-9MdKsAVzXGBlhxlKrySdK70JEMn2JGAAHFtxlQqvxKn9mGcgCGbqxr7G3NqLvQLrLKxbs1uFwbbrNEbiQUK0md2VRSUVlBpvopSKxUl7FuWrSJbQP7eO2mA9pVB6jpO1MYaPfqrv+6TzP6C3FPh5idbjWWylV5Wja2wH+LNjgZWAK1GeZ+3jel0j7rDn3NYs5CMf6VXaQZVH0bFzHENctZ+aV-ZBnWfOUAASWkQvQniABmAAWKFP0ad1+z8qEdAQUBx2bjzW6OEuADltz8t4YEmXGKvxjmifCtC8+ZwuBmL7dy8rmv66ORu3Xc6Le7WdvO+7nesKhAeh6wkex9MS80tvLJsFSKBsG4eAXUacPdlqIrvxaTm-wn-pAITD5gLHIQsIKrFPrsce3xRY1lzpAzC-kpZ-0jDLDSjRVaknQSgVOKA6QzQTs9a6ut2JfQNn7fSAddr7TBqWS20MDaRy2tHZ2h1XZzXdsQz22DMEIIoX6E2Rlg5LW3Ewm6KC7olxgOXQhlYk5ZxTtuNOZQM4OQxo7f+PY+7Lwrv0Kudckoi1+ATaeG5tG7BXnotehjr6U1vpkSw71yI1BgAAKQgMqd+hhMj7xAOOdmP8hp4wAUMAUwEZgl0FrDdAG4n7AEcVAOAEByJQFOCXcu0D5ywOxECOJCSkkpLSTogiWcgkkX6AAKw8WgTBVTlS4PwRrN22suFsTAGQsB-CAZymoebNhkMrZnUYXpARLDaFZm0LIlp-02m8J0V0vibSZQlzEaMiRMsVnNJkAtT26QFI4O3CrbcukdmLLEtgfZyzRFu2yd2OpNSlF4LomAVRUsua1hWJkyeP9TGrAvKlOxGVMjJHiYuRMsBgDYCfoQCoVRP5swJmU7GAxqq1Xqo1YwIt5FwLUaUsZIBuB4AUFC5AIBYVoFJLNJ6nCZkwAJeCylqzulA3pXgGUckmVnNZVAEOYCNAjOZfxblMp443OxTkmQ712IoAlK89RcisaoS+QuExoU1wbn+VeIAA)



This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
