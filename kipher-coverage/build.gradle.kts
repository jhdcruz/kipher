plugins {
    id("conventions.module")
    id("jacoco-report-aggregation")
}

dependencies {
    jacocoAggregation(projects.kipherCommon)
    jacocoAggregation(projects.kipherAes)
}

tasks {
    test {
        finalizedBy(testCodeCoverageReport)
    }
}
