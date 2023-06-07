import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import javax.inject.Inject

/**
 * Common build properties used to build Kipher subprojects.
 *
 * Default values are set in the root `gradle.properties`, and can be overridden via
 * [CLI args, system properties, and environment variables](https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties)
 */
abstract class KipherBuildProperties @Inject constructor(
    private val providers: ProviderFactory,
) {
    /**
     * Version to be used for builds
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val compilerVersion: Provider<JavaLanguageVersion> =
        kipherProperty("toolchain.compilerVersion", JavaLanguageVersion::of)

    /**
     * Version that should be used to run tests.
     *
     * This value can set in CI/CD environments. Defaults to 8
     */
    val testLauncher: Provider<JavaLanguageVersion> =
        kipherProperty("toolchain.testLauncher", JavaLanguageVersion::of)
            .orElse(compilerVersion)

    /**
     * The Kotlin language level that artifacts are compiled to support.
     */
    val languageLevel: Provider<KotlinVersion> =
        kipherProperty("toolchain.languageLevel", KotlinVersion::fromVersion)

    private fun <T : Any> kipherProperty(name: String, convert: (String) -> T) =
        providers.gradleProperty("kipher.$name").map(convert)

    companion object {
        const val EXTENSION_NAME = "kipherBuild"
    }
}
