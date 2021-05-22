# Tennis Club

Wiki to the project is [here](https://github.com/Fuyunori/PA165-project/wiki).

## System Requirements

- OpenJDK 11
- Maven 3
- Node.js 14

## Building and Running

To build and serve both the back-end and the front-end of the application, do the following:

```shell
cd ui  # traverse to the UI's root directory
npm install  # install the UI's dependencies
npm run build  # build the Angular app
cd ..  # return to the project root
mvn clean install  # install deps, build and test the Spring Boot app
cd rest  # traverse to the Rest submodule's directory
mvn spring-boot:run  # run the Spring Boot app
```

- The UI will be served at `http://localhost:8080/pa165`.
- The REST API will be available at `http://localhost:8080/pa165/rest`.

## UI Development Build

For more convenient UI development (auto-rebuild+reload on file changes):

- build and serve the back-end in the same way as described above;
- run Angular's dev-server by executing `npm start` in the `ui` directory.

The UI will be served at `http://localhost:4200/pa165`.
