plugins {
    application
    id("com.github.johnrengelman.shadow")
}

tasks.shadowJar {
    archiveBaseName.set(project.name)
    archiveVersion.set("")
    archiveClassifier.set("")
}
