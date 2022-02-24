import java.util.*

plugins {
    `maven-publish`
    signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    publications.withType<MavenPublication> {

        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        pom {

            name.set("uncaught-exception-handling")
            description.set("Ever wanted to use something looking like Java's Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);? Well now you can!")
            url.set("https://github.com/Vinccool96/uncaught-exception-handling")

            licenses {
                license {
                    name.set("Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                    comments.set("A business-friendly OSS license")
                }
            }
            developers {
                developer {
                    id.set("Vinccool96")
                    name.set("Vincent Girard")
                    email.set("vinccool96@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:github.com/Vinccool96/uncaught-exception-handling.git")
                developerConnection.set("scm:git:ssh://github.com/Vinccool96/uncaught-exception-handling.git")
                url.set("https://github.com/Vinccool96/uncaught-exception-handling")
            }
        }
    }
}
// Signing artifacts. Signing.* extra properties values will be used

signing {
    sign(publishing.publications)
}