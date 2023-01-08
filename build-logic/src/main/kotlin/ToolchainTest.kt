import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.register

val Project.javaToolchains: JavaToolchainService
    get() = extensions.getByType(JavaToolchainService::class.java)

fun Project.addAdditionalJdkVersionTests() {
    listOf(8, 11, 17).forEach(::addJdkVersionTests)
}

private fun Project.addJdkVersionTests(jdkVersion: Int) {
    val jdkVersionTests = tasks.register<Test>("testOnJdk$jdkVersion") {
        javaLauncher.set(
            javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(jdkVersion))
            }
        )
        val jvmArgs = mutableListOf<String>()

        setJvmArgs(jvmArgs)
    }
    tasks.named("check") {
        dependsOn(jdkVersionTests)
    }
}
