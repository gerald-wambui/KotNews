package com.jaguh.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is a startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */


/*
 *Run this benchmark in Android studio to see startup measurements and capture system traces to
 * investigate the performance of your app from a cold state
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class ColdStartupBenchmark: AbstractStartupBenchmark(StartupMode.COLD)





abstract class AbstractStartupBenchmark(private val startupMode: StartupMode) {
	@get:Rule
	val benchmarkRule = MacrobenchmarkRule()

	@Test
	@RequiresApi(Build.VERSION_CODES.N)
	fun startupNoCompilation() = startup(CompilationMode.None())


	@Test
	@RequiresApi(Build.VERSION_CODES.N)
	fun startupBaselineProfileDisabled() = startup(
		CompilationMode.Partial(
			baselineProfileMode = BaselineProfileMode.Disable,
			warmupIterations = 1
		),
	)

	@Test
	@RequiresApi(Build.VERSION_CODES.N)
	fun startupBaselineProfile() = startup(
		CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require)
	)

	@Test
	fun startupFullCompilation() = startup(CompilationMode.Full())


	private fun startup(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
		packageName = PACKAGE_NAME,
		metrics = listOf(StartupTimingMetric()),
		compilationMode = compilationMode,
		iterations = 5,
		startupMode = startupMode,
		setupBlock = {
			pressHome()
		},
	) {
		startActivityAndWait()
	}
}


@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
	@get:Rule
	val benchmarkRule = MacrobenchmarkRule()

	@Test
	fun startup() = benchmarkRule.measureRepeated(
		packageName = PACKAGE_NAME,
		metrics = listOf(StartupTimingMetric()),
		iterations = 5,
		startupMode = StartupMode.COLD
	) {
		pressHome()
		startActivityAndWait()
	}
}