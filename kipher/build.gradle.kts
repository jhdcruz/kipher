plugins {
    id("kipher-module")
    id("kipher-publish")
    id("jacoco-report-aggregation")
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

tasks.test {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}
