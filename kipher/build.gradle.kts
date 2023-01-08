plugins {
    id("kipher-common")
    id("kipher-publish")
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":kipher-aes"))
}

reporting {
    reports {
        creating(JacocoCoverageReport::class) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}
