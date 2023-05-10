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

tasks.named<JacocoReport>("testCodeCoverageReport") {
    dependsOn(tasks.test)
}
