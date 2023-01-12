plugins {
    id("kipher-common")
    id("kipher-publish")
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.kipherAes)
}

@Suppress("UnstableApiUsage")
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
