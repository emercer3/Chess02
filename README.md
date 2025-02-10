# ♕ BYU CS 240 Chess

Server design website
[serverdesign](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9FMr73H0XwfksKyTAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatLlqOJpEuGFocjA3K8gagrCjAwGhhRbqdjh5Q3KyWjyI6zoEvhLLlJ63pBgGQYhq60qRtRwA2jA07xnKuHJjBJZoTy2a5pg-4gnBJRXEBpGjJBNZBrOzYTFB7Z6YU2Q9jA-aDr0RkjiZVlmXWjaWaBMAHGYnCrt4fiBF4KDoHuB6+Mwx7pJkmD2ReRTUNe0gAKK7ml9Rpc0LQPqoT7dNOFloDZbK6eUxXeaVulshxMBIfY0WoVFvoYRi2HKRqgmkjA5JgLG-pVXO5FMmxMnlAAZjyHCsvR2gwMkGSpAp5nVSxY3Sfp4J2gK2h8YmAmsUJRgoNwmSDeJXkjRtZoRoUlqnedagwINJUHSpf5qWmaHRVpCB5t97FXmmPTfiDnaJWAfYDjA3SlkugWeMFG6Qrau7QjAADio6srFp4JeezD6deWNZbl9ijkVa1zmVqlAiWw3oDpQPbfKDXQgoyo46MqiwjzKDtVhH3dcdvX9ZdTNoKNd1UeUJRINNL1SyLeFi+6HOxALfNSzLlETTA02cKywDKtjuO3frbMIa9Zva6r9OpuUAuohi-2AwztnJQZpZTJTvPjJU-T+ygACS0iBwAjL2ADMAAsTwnpkBoVh5Uw6AgoANino6mX7o4AHKjlZewwI04MpV7UMw85gF9CHqiBxUwejuHUexwnUxJ-qxkgV8GdZzn7mgU8IdF6MJdl-5y5BeugTYD4UDYNw8C6pk5ujCkcVnjkxOXpX5Q3g0FNU8ENPoEOY+jhXxyO8Chn14XudWX5LOe3VynCWvKAC7CImZC7FAaIOqqyOptcWFJJbn2lpbcaD1qIKyVibaBoCZA9Q1v-H+o5YRX1GHrOB7JKo2lZCHB2X1PbO1HK7WI7s36pmBgfX2j9Rht3KNHeOr8fyQyJjXIc+cWERzYR3V+M9kZzwCJYM6SFkgwAAFIQB5BvQwAQB4gAbITXeH9GHVEpHeFoIdqbXQvr0ZewBJFQDgBAJCUBZgh3DjfDsd8AKmPMZY6xtjW7SGgu-a25QABWCi0C-wCTyQBwDhY4VFuA90EsxK61gdJeB8tKCK1QMgoxpVIlq2iYRMAv87HSHwYkwhfUKQkNHKgqSvU-A8SwaMWELjKBuOgEU80Bsal6nKeKLJTi0whKCVQoBmEwC0NqkmfePswZ01ONXRyA4hyIxXGIkKAQvBmK7F6WAwBsDL0IPERIW8CZQy0T7Co6VMrZVysYOm5CnYwEVMqOhsEJk7RANwPACgdnIBAPstAsJOrwSiXdYS7y4TSDOhSFAkl0FyxgG8zZJszY8WAGQs4FCZAQsyIyUZrMSagwcVXHhcza6LMwEAA)



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
