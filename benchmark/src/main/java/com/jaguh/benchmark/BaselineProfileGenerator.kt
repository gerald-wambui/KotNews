package com.jaguh.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test


@RequiresApi(Build.VERSION_CODES.P)
class BaselineProfileGenerator {
	@get:Rule
	val baselineProfileRule = BaselineProfileRule()


	@Test
	fun startup() = baselineProfileRule.collect(
		packageName = PACKAGE_NAME,
		stableIterations = 2,
		maxIterations = 8,
	){
		pressHome()
		/**
		 * This block defines the app's critical user journey. We are interested in optimizing
		 * for app startup. But you can also navigate and scroll
		 * through your most important app UI
		 */

		startActivityAndWait()
		device.waitForIdle()

		//navigate to details screen
		device.testDiscover() || return@collect
		device.navigateFromMainToDetails()
		device.pressBack()
	}
}
