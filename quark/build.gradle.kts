plugins {
    java
    id("io.freefair.lombok") version "8.6"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {

    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation(libs.guava)
    implementation(libs.quarkusRest)
    implementation(libs.lombok)
    implementation(libs.quarkusHibernateOrm)
    implementation(libs.quarkusAgroal)
    implementation(libs.quarkusFlyway)
    implementation(libs.quarkusRestJsonB)
//    implementation(libs.quarkusHibernateValidator)
    implementation(libs.quarkusSmallRyeOpenApi)
    implementation(libs.quarkusSpringDataJpa)
    implementation(libs.springDataJpa)
    implementation(libs.quarkusJdbcPostgresql)
    implementation(libs.quarkusArc)
    implementation(libs.jsoup)
    implementation(libs.telegramBots)
    implementation(libs.jacksonGuava)
    implementation(libs.plexApi)
    implementation(libs.theMovieDbApi)
    implementation(libs.slf4jlog4j12)
    implementation(libs.log4jApi)
    implementation(libs.log4jCore)
    implementation(libs.plexApi)
    implementation(libs.apacheHttpCore)
    implementation(libs.apacheHttpClient)
    implementation(libs.apacheCommonsLang)
    implementation(libs.apacheCommonsText)
    implementation(libs.apacheCommonsValidator)
    implementation(libs.apacheCommonsCollections)
    implementation(libs.agroalPool)

    testImplementation(libs.quarkusJdbcH2)
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-junit5-mockito")
    testImplementation("io.rest-assured:rest-assured")
//    testImplementation(libs.mockitoCore)
//    testImplementation(libs.junit)
//    testImplementation(libs.junitJupiterEngine)
}

group = "org.acme"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
