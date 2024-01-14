/*
 * Designed and developed by 2024 jaguh (gerald-wambui)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
	id("com.android.test")
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "com.jaguh.benchmark"
	compileSdk = 34

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	kotlinOptions {
		jvmTarget = "1.8"
	}

	defaultConfig {
		minSdk = 24
		targetSdk = 34

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		// This benchmark buildType is used for benchmarking, and should function like your
		// release build (for example, with minification on). It"s signed with a debug key
		// for easy local/CI testing.
		create("benchmark") {
			isDebuggable = true
			signingConfig = getByName("debug").signingConfig
			matchingFallbacks += listOf("release")
		}
	}

	targetProjectPath = ":app"
	experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
	implementation("androidx.test.ext:junit:1.1.5")
	implementation("androidx.test.espresso:espresso-core:3.5.1")
	implementation("androidx.test.uiautomator:uiautomator:2.2.0")
	implementation("androidx.benchmark:benchmark-macro-junit4:1.2.2")
}

androidComponents {
	beforeVariants(selector().all()) {
		it.enable = it.buildType == "benchmark"
	}
}