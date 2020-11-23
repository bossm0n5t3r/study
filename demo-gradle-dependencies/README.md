# Demo Gradle Dependencies

## References

[http://andresalmiray.com/maven-dependencies-pop-quiz-results/](http://andresalmiray.com/maven-dependencies-pop-quiz-results/)

## problem 1

### 30.0-jre first, 29.0-jre second
```
dependencies {
    ...
    implementation("com.google.guava:guava:30.0-jre")
    implementation("com.google.guava:guava:29.0-jre")
    ...
}
```

### result
```
com.google.guava:guava:29.0-jre
```

## problem 2

### 30.0-jre first, 29.0-jre second
```
dependencies {
    ...
    implementation("com.google.guava:guava:29.0-jre")
    implementation("com.google.guava:guava:30.0-jre")
    ...
}
```

### result
```
com.google.guava:guava:30.0-jre
```


