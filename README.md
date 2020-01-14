[![Build Status](https://travis-ci.org/kikkia/edmtrain-client.svg?branch=master)](https://travis-ci.org/kikkia/edmtrain-client)[![](https://jitpack.io/v/kikkia/edmtrain-client.svg)](https://jitpack.io/#kikkia/edmtrain-client)


# edmtrain-client
A java/kotlin api client for the [Edmtrain api](https://edmtrain.com/developer-api.html). Please follow the [Edmtrain api terms of use](https://edmtrain.com/api-terms-of-use.html). 

#### Currently under development. Wait for a v1.0 release before consumption.

## Importing this library
You need to make sure you have the jitpack repository added to your project.
### Maven
Add the jitpack repo to your repositories:
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Add the dependancy: 
```xml
<dependency>
  <groupId>com.github.kikkia</groupId>
  <artifactId>edmtrain-client</artifactId>
  <version>Tag</version>
</dependency>
```

### Gradle
Add the jitpack repo to your repositories:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependancy:
```gradle
dependencies {
  implementation 'com.github.kikkia:edmtrain-client:Tag'
}
```

## Usage
In order to use the client you will need an api token to the [Edmtrain API](https://edmtrain.com/developer-api.html). Once you have obtained a key you can create a client.
```java
EdmtrainClient.Builder builder = new EdmtrainClient.Builder();
builder.setToken("YOUR_API_TOKEN");
EdmtrainClient client = builder.build();
```

To query for events you can utilize the client and the EventQuery object. You can also limit the results of the query through many parameters. 
```java
EdmtrainClient.EventQuery query = client.queryForEvent();
query.withEventName("EventName");
List<Event> events = query.get();
```
