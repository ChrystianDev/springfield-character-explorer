apply(plugin = "jacoco")

tasks.register<JacocoReport>("jacocoTestDevDebugUnitTestReport") {
    dependsOn("testDevDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(file("${layout.buildDirectory}/reports/jacoco/testDevDebugUnitTest/index.html"))
    }

    classDirectories.setFrom(
        fileTree("${layout.buildDirectory}/tmp/kotlin-classes/debug") {
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*"
            )
        }
    )

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )

    // Ruta para variantes con flavors (devDebug)
//    val debugTree = fileTree(
//        mapOf(
//            "dir" to "${layout.buildDirectory}/tmp/kotlin-classes/devDebug",
//            "excludes" to fileFilter
//        )
//    )
    val debugTree = fileTree(
        "${layout.buildDirectory}/intermediates/javac/devDebug/classes"
    ) {
        exclude(fileFilter)
    }

    val kotlinDebugTree = fileTree(
        "${layout.buildDirectory}/tmp/kotlin-classes/devDebug"
    ) {
        exclude(fileFilter)
    }

    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree, kotlinDebugTree))
//    executionData.setFrom(
//        file("${layout.buildDirectory}/jacoco/testDevDebugUnitTest.exec")
//    )
    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include("jacoco/testDevDebugUnitTest.exec")
        }
    )
}

tasks.register("coverage") {
    group = "verification"
    dependsOn("createDevDebugUnitTestCoverageReport")
}