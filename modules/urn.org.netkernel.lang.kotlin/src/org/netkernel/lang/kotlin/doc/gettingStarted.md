#MARKDOWN

# Getting Started

It is recommended to use Gradle for building NetKernel modules which use code written in Kotlin. The following
example should be enough to get a simple NetKernel module working using Kotlin. If you're new to using
Gradle to build NetKernel modules, there's a a [Gradle Plugin Guide](http://docs.netkernel.org/book/view/book:org:netkernel:gradle:plugin:book/).

## Gradle Example

### build.gradle

{java}
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url "https://maven.netkernel.org/netkernel-maven"
        }
    }

    dependencies {
        classpath group: 'urn.org.netkernel', name: 'gradle.plugin', version: '[1.1.4,)'
    }
}

plugins {
    id 'idea'
    id 'org.jetbrains.kotlin.jvm' version '1.3.60'
}

apply plugin: 'netkernel'

sourceSets["main"].java.srcDirs("src")
sourceSets["main"].kotlin.srcDirs("src")
sourceSets["main"].resources.srcDirs("src")

sourceSets["main"].java.exclude("**/*.kts")
sourceSets["main"].kotlin.exclude("**/*.kts")

netkernel {
    useMavenCentral()
    useMavenNK()
    useStandardCompileDependencies()

    instances {
        NK {
            edition = "SE"
            location = "../NKSE-5.2.1/"
        }
    }
}

compileKotlin {
    kotlinOptions{
        jvmTarget = "11"
    }
}

dependencies {
    compileOnly group: 'urn.org.netkernel', name: 'lang.kotlin', version: '[1.1.1,)'
}
{/java}

### src/module.xml

{xml}
<module version="2.0">
    <meta>
        <identity>
            <uri>urn:org:netkernel:lang:kotlin:example</uri>
            <version>1.1.1</version>
        </identity>
        <info>
            <name>Lang / Kotlin / Example</name>
            <description>Module demonstrating how to use lang-kotlin</description>
        </info>
    </meta>

    <system>
        <dynamic/>
    </system>

    <rootspace>
        <mapper>
            <config>
                <endpoint>
                    <grammar>res:/kotlin/example/one</grammar>
                    <request>
                        <identifier>active:java</identifier>
                        <argument name="class">org.netkernel.lang.kotlin.example.AccessorOne</argument>
                    </request>
                </endpoint>
                <endpoint>
                    <grammar>res:/kotlin/example/two</grammar>
                    <request>
                        <identifier>active:kotlinScript</identifier>
                        <argument name="operator">res:/org/netkernel/lang/kotlin/example/two.nk.kts</argument>
                    </request>
                </endpoint>
            </config>
            <space>
                <fileset>
                    <regex>res:/org/netkernel/lang/kotlin/example/two.nk.kts</regex>
                </fileset>

                <import>
                    <uri>urn:org:netkernel:lang:kotlin</uri>
                    <private/>
                </import>
            </space>
        </mapper>

        <import>
            <uri>urn:org:netkernel:ext:layer1</uri>
            <private/>
        </import>
    </rootspace>
</module>
{/xml}

### src/org/netkernel/lang/kotlin/example/AccessorOne.kt

{kotlin}
package org.netkernel.lang.kotlin.example

import org.netkernel.lang.kotlin.knkf.context.SourceRequestContext
import org.netkernel.lang.kotlin.knkf.endpoints.KotlinAccessor

class AccessorOne: KotlinAccessor() {
    override fun SourceRequestContext.onSource() {
        response {
            "Hello World!"
        }
    }
}
{/kotlin}

### src/org/netkernel/lang/kotlin/example/two.nk.kts

{kotlin}
onSource {
    response {
        "Hello World!"
    }
}
{/kotlin}
