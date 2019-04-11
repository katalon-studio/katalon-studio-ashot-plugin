package kms.turing.katalon.plugins.visualtesting

import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.nio.file.Paths

import javax.imageio.ImageIO

import org.apache.commons.io.FileUtils
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory

import kms.turing.katalon.plugins.common.BaseKeyword
import kms.turing.katalon.plugins.helper.DateTimeHelper
import kms.turing.katalon.plugins.helper.FileHelper
import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.Screenshot
import ru.yandex.qatools.ashot.coordinates.Coords
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider
import ru.yandex.qatools.ashot.shooting.ShootingStrategies

public class ScreenCapture extends BaseKeyword{

	static final String screenshotDir = RunConfiguration.getProjectDir() + File.separator + "Screenshots"
	static final Color ignoredColor = Color.GRAY
	static final int scrollTimeout = 100
	/**
	 * Take screenshot of the whole screen using AShot and save to screenshots folder in current working project
	 * @param filename the image will be save with the name
	 */
	@Keyword
	static void takeScreenshot(String filename, FailureHandling flowControl = FailureHandling.OPTIONAL){
		try{
			def screenshot = new AShot().takeScreenshot(DriverFactory.getWebDriver())
			saveScreenshot(filename, screenshot)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Take screenshot of the whole screen and then cut off it's header & footer using AShot and save to screenshots folder in current working project
	 * @param filename the image will be save with the name
	 * @headerToCut the width of header to cut off
	 * @footerToCut the width of footer to cut off
	 */
	@Keyword
	static void takeCuttingScreenshot(String filename, int headerToCut, int footerToCut, FailureHandling flowControl = FailureHandling.OPTIONAL){
		try{
			def screenshot = new AShot()
					.shootingStrategy(ShootingStrategies.cutting(headerToCut, footerToCut))
					.takeScreenshot(DriverFactory.getWebDriver())
			saveScreenshot(filename, screenshot)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Take screenshot of the whole screen and then scale image according to device pixel ratio using AShot and save to screenshots folder in current working project
	 * @param filename the image will be save with the name
	 * @dpr device pixel ratio
	 */
	@Keyword
	static void takeScalingScreenshot(String filename, float dpr, FailureHandling flowControl = FailureHandling.OPTIONAL){
		try{
			def screenshot = new AShot()
					.shootingStrategy(ShootingStrategies.scaling(dpr))
					.takeScreenshot(DriverFactory.getWebDriver())
			saveScreenshot(filename, screenshot)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Take screenshot of the entire page using AShot and save to screenshots folder in current working project
	 * @param filename the image will be save with the name
	 */
	@Keyword
	static void takeEntirePageScreenshot(String filename, FailureHandling flowControl = FailureHandling.OPTIONAL){
		try{
			def screenshot = new AShot()
					.shootingStrategy(ShootingStrategies.viewportPasting(scrollTimeout))
					.takeScreenshot(DriverFactory.getWebDriver())
			saveScreenshot(filename, screenshot)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Take the picture of a specific web element using AShot and save to screenshots folder in current working project
	 * @param object the web element need to take picture
	 * @param filename the image will be save with the name
	 */
	@Keyword
	static void takeWebElementScreenshot(TestObject object, String filename, int timeout = 10, FailureHandling flowControl = FailureHandling.OPTIONAL){
		try{
			info "Take screenshot of object $object within $timeout seconds"
			def timeoutMsg = "Fail to take web element screenshot due to timeout"
			def driver = DriverFactory.getWebDriver()
			def startTime = Calendar.getInstance().getTime()
			def element = WebUiCommonHelper.findWebElement(object, timeout)
			def remainingTime = timeout - DateTimeHelper.getElapsedTime(startTime, Calendar.getInstance().getTime())
			handleErrorIf(remainingTime <= 0, timeoutMsg, FailureHandling.STOP_ON_FAILURE)

			WebDriverWait wait = new WebDriverWait(DriverFactory.getWebDriver(), remainingTime)
			wait.until(ExpectedConditions.visibilityOf(element))

			def screenshot = new AShot()
					.shootingStrategy(ShootingStrategies.viewportPasting(scrollTimeout))
					.takeScreenshot(driver, element)
			info "Save the screenshot into file: $filename"
			saveScreenshot(filename, screenshot)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * take the picture of a specific test element using AShot and save to screenshots folder in current working project
	 * @param object the web element need to take picture
	 * @param filename the image will be save with the name
	 */
	@Keyword
	static void takeWebElementsScreenshot(List<TestObject> objects, String filename, int timeout = 10, FailureHandling flowControl = FailureHandling.OPTIONAL){
		try{
			info "Take screenshot of objects $objects within $timeout seconds"
			def timeoutMsg = "Fail to take web element screenshot due to timeout"
			def driver = DriverFactory.getWebDriver()
			def startTime = Calendar.getInstance().getTime()
			def remainingTime = timeout - DateTimeHelper.getElapsedTime(startTime, Calendar.getInstance().getTime())
			List<WebElement> elements = []
			objects.each{ obj ->
				elements.addAll(WebUiCommonHelper.findWebElements(obj, remainingTime))
				remainingTime = timeout - DateTimeHelper.getElapsedTime(startTime, Calendar.getInstance().getTime())
			}
			handleErrorIf(remainingTime <= 0, timeoutMsg, FailureHandling.STOP_ON_FAILURE)

			WebDriverWait wait = new WebDriverWait(DriverFactory.getWebDriver(), remainingTime)
			wait.until(ExpectedConditions.visibilityOfAllElements(elements))

			def screenshot = new AShot()
					//.imageCropper(new IndentCropper().addIndentFilter(new BlurFilter()))
					.shootingStrategy(ShootingStrategies.viewportPasting(scrollTimeout))
					.takeScreenshot(driver, elements)
			info "Save the screenshot into file: $filename"
			saveScreenshot(filename, screenshot)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Take the picture of a specific test element using AShot and save to screenshots folder in current working project
	 * @param object the web element need to take picture
	 * @param ignoreObjs the web elements will be excluded in the captured image
	 * @param filename the image will be save with the name
	 */
	@Keyword
	static void takeElementScreenshotIgnoringAreas(TestObject object, String filename, List<TestObject>ignoreObjs, int timeout = 10, FailureHandling flowControl = FailureHandling.OPTIONAL){
		info "Take screenshot of: $object with ignoring areas of: $ignoreObjs"
		try{
			WebDriver driver = DriverFactory.getWebDriver()
			WebElement element = WebUiCommonHelper.findWebElement(object, timeout)
			def startTime = Calendar.getInstance().getTime()
			def remainingTime = timeout - DateTimeHelper.getElapsedTime(startTime, Calendar.getInstance().getTime())

			List<WebElement> ignoredElements = []
			ignoreObjs.each{ obj ->
				ignoredElements.add(WebUiCommonHelper.findWebElement(obj, remainingTime))
				remainingTime = timeout - DateTimeHelper.getElapsedTime(startTime, Calendar.getInstance().getTime())
			}

			Screenshot screenshot = new AShot()
					.shootingStrategy(ShootingStrategies.viewportPasting(scrollTimeout))
					.takeScreenshot(driver, element)
			Coords originShif = screenshot.getOriginShift()
			Set<Coords> ignoredAreas = new WebDriverCoordsProvider().ofElements(driver, ignoredElements)
			Coords shootingArea = new WebDriverCoordsProvider().ofElement(driver, element)
			markGrayIgnoredAreas(screenshot, shootingArea, ignoredAreas)
			saveScreenshot(filename, screenshot)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Add the image into baseline folder, return full path of baseline image
	 * @param filename
	 * @param baselineFolder
	 * @return String
	 */
	@Keyword
	static String baselineImage(String filename, String baselineDir, FailureHandling flowControl = FailureHandling.OPTIONAL){
		try{
			info "Add the image file with name: $filename into baseline dir: $baselineDir"
			Path baselinePath = Paths.get(baselineDir)
			Path screenshotPath = Paths.get(screenshotDir)
			if(!baselinePath.toFile().exists()){
				baselinePath.toFile().mkdir()
			}

			File to = baselinePath.resolve(filename).toFile()
			File from = screenshotPath.resolve(filename).toFile()
			FileUtils.copyFile(from, to)

			return to.getAbsolutePath()
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	private static void saveScreenshot(String filename, Screenshot screenshot){
		String workingFolder = RunConfiguration.getProjectDir()
		String extension = FileHelper.getFileExtension(filename)

		Path screenshotPath = Paths.get(screenshotDir)

		if(!screenshotPath.toFile().exists()){
			screenshotPath.toFile().mkdir()
		}
		ImageIO.write(screenshot.getImage(), extension, screenshotPath.resolve(filename).toFile())
	}

	private static void markGrayIgnoredAreas(Screenshot screenshot, Coords shootingArea, Set<Coords>ignoredAreas){
		BufferedImage image = screenshot.getImage()
		Coords imageArea = Coords.ofImage(image)
		for(Coords coords in ignoredAreas){
			coords.x -= shootingArea.location.x
			coords.y -= shootingArea.location.y
		}

		Set<Coords> intersections = Coords.intersection(ignoredAreas, [imageArea])
		for(Coords coords in intersections){
			for(int i = coords.x; i<coords.width; i ++){
				for(int j = coords.y; j < coords.height; j ++){
					image.setRGB(i, j, ignoredColor.getRGB())
				}
			}
		}
	}
}
