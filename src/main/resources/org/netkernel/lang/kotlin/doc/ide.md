#MARKDOWN

# IDE Support

## Kotlin

If you're writing non-script Kotlin, then, assuming your IDE has Kotlin support, setting up code completion will be as
simple as making sure that both the Kotlin standard library Jars and the NetKernel Kotlin module Jar are on your classpath.
If you're using a build system such as Gradle (and the NetKernel Gradle plugin), then you will probably get this for free.

## Kotlin Script

Getting code completion working for Kotlin Script is more complicated. Kotlin Script is still an experimental feature.
This author has had success getting code completion working for Kotlin Script working in IntelliJ. Please let us know
if you manage to get this working in other IDEs.

### IntelliJ

In order to get Code Completion working correctly, you will need to:

* use `.nk.kts` as the extension for your Kotlin Script files,
* have the lang-kotlin module Jar as a compile dependency,
* in *Preferences > Build, Execution, Deployment > Compiler > Kotlin Compiler*, set the following settings in the
'Kotlin Scripting' section:
  * 'Script template classes': `org.netkernel.lang.kotlin.script.NetKernelKotlinScript`,
  * 'Script template classpath': `/path/to/urn.org.netkernel.lang.kotlin.jar`.
* The above should lead to a row for 'NetKernel Kotlin Script' appearing in  *Language & Frameworks > Kotlin > Kotlin Scripting*,
you will need to reorder the list so that it is before the entry for `.kts`.
