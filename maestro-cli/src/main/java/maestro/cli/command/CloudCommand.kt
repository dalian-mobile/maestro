/*
 *
 *  Copyright (c) 2022 mobile.dev inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package maestro.cli.command

import maestro.cli.api.ApiClient
import maestro.cli.cloud.CloudInteractor
import picocli.CommandLine
import picocli.CommandLine.Option
import java.io.File
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "cloud",
    description = [
        "Tests your application on Maestro Cloud (https://docs.mobile.dev)",
        "Provide your application file and a folder with Maestro flows to run them in parallel on multiple devices in Maestro Cloud",
        "By default, the command will block until all analyses have completed. You can use the --async flag to run the command asynchronously and exit immediately.",
    ]
)
class CloudCommand : Callable<Int> {

    @CommandLine.Parameters(description = ["App binary to run your Flows against"])
    private lateinit var appFile: File

    @CommandLine.Parameters(description = ["Flow file or directory"])
    private lateinit var flowFile: File

    @Option(order = 0, names = ["--apiKey"], description = ["API key"])
    private var apiKey: String? = null

    @Option(order = 1, names = ["--apiUrl"], description = ["API base URL"])
    private var apiUrl: String = "https://api.mobile.dev"

    @Option(order = 2, names = ["--mapping"], description = ["dSYM file (iOS) or Proguard mapping file (Android)"])
    private var mapping: File? = null

    @Option(order = 3, names = ["--repoOwner"], description = ["Repository owner (ie: GitHub organization or user slug)"])
    private var repoOwner: String? = null

    @Option(order = 4, names = ["--repoName"], description = ["Repository name (ie: GitHub repo slug)"])
    private var repoName: String? = null

    @Option(order = 5, names = ["--branch"], description = ["The branch this upload originated from"])
    private var branch: String? = null

    @Option(order = 6, names = ["--pullRequestId"], description = ["The ID of the pull request this upload originated from"])
    private var pullRequestId: String? = null

    @Option(order = 7, names = ["-e", "--env"], description = ["Environment variables to inject into your Flows"])
    private var env: Map<String, String> = emptyMap()

    @Option(order = 8, names = ["--name"], description = ["Name of the upload"])
    private var uploadName: String? = null

    @Option(order = 9, names = ["--async"], description = ["Run the upload asynchronously"])
    private var async: Boolean = false

    @Option(order = 10, names = ["--android-api-level"], description = ["Android API level to run your flow against"])
    private var androidApiLevel: Int? = null

    override fun call(): Int {
        return CloudInteractor(
            client = ApiClient(apiUrl),
        ).upload(
            async = async,
            flowFile = flowFile,
            appFile = appFile,
            mapping = mapping,
            env = env,
            uploadName = uploadName,
            repoOwner = repoOwner,
            repoName = repoName,
            branch = branch,
            pullRequestId = pullRequestId,
            apiKey = apiKey,
            androidApiLevel = androidApiLevel,
        )
    }

}
