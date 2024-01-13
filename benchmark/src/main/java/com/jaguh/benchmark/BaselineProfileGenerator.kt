package com.jaguh.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
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

private fun UiDevice.testDiscover(): Boolean{
	return wait(Until.hasObject(By.res(PACKAGE_NAME, "transformationLayout")), 1_000)
}


private fun UiDevice.navigateFromMainToDetails() {
	waitForObject(By.res(PACKAGE_NAME, "transformationLayout")).click()
	wait(Until.hasObject(By.res(PACKAGE_NAME, "nestedScroll")), 1_000)
	waitForObject(By.res(PACKAGE_NAME, "nestedScroll")).scroll(Direction.DOWN, 1f)
	waitForIdle()
	pressBack()
}


private fun UiDevice.waitForObject(selector: BySelector, timeout: Long = 5_000): UiObject2 {
	if (wait(Until.hasObject(selector), timeout)) {
		return findObject(selector)
	}

	error("Object with selector [$selector] not found")
}