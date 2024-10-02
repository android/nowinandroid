FROM cimg/android
WORKDIR /app
COPY . /app
RUN /app/gradlew androidDependencies
RUN /app/gradlew androidDependencies
RUN /app/gradlew lint test
