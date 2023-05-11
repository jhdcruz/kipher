plugins {
    id("kipher-module")
    id("jacoco-report-aggregation")
}

dependencies {
    jacocoAggregation(projects.kipherAes)
    jacocoAggregation(projects.kipherRsa)
}

@Suppress("UnstableApiUsage")
reporting {
    reports {
        creating(JacocoCoverageReport::class) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

tasks.named<Test>("test") {
    finalizedBy(tasks.named<JacocoReport>("testCodeCoverageReport"))
}
