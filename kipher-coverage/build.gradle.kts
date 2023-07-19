plugins {
    id("conventions.module")
    id("jacoco-report-aggregation")
}

dependencies {
    jacocoAggregation(projects.kipherAes)
    jacocoAggregation(projects.kipherDigest)
    jacocoAggregation(projects.kipherMac)
}

tasks {
    test {
        finalizedBy(testCodeCoverageReport)
    }
}
