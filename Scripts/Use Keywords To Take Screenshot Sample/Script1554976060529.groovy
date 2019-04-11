import org.openqa.selenium.WebDriver as WebDriver

import com.kms.katalon.core.annotation.TearDown
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

baseline = true
baselineDir = System.getProperty('user.dir') + "\\baseline"
screenshotDir = RunConfiguration.getProjectDir() + "\\Screenshots"
filename = "logo.png"

'Open the site'
WebUI.openBrowser('https://dictionary.cambridge.org/')
'Maximize window'
DriverFactory.getWebDriver().manage().window().fullscreen()


'Take screenshot of the page within screen size'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeScreenshot'('fullscreen.png', FailureHandling.OPTIONAL)

'Take screenshot of the page within screen size and cut header and footer'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeCuttingScreenshot'('cutting_screen.png',100, 50, FailureHandling.OPTIONAL)

'Take screenshot of the page within screen size and scale image according to dpr'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeScalingScreenshot'('scaling_screen.png',2 , FailureHandling.OPTIONAL)


'Take screenshot of the full page'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeEntirePageScreenshot'('fullpage_screen.png', FailureHandling.OPTIONAL)


'Take screenshot of the logo'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeWebElementScreenshot'(ObjectRepository.findTestObject('Object Repository/Cambridge/img_Logo'), filename,
													  10, FailureHandling.OPTIONAL)
if(baseline){
	'Baseline the image of logo'
	CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.baselineImage'(filename, baselineDir, FailureHandling.OPTIONAL)
}

'Compare logo is matched the baselined image'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ImageComparison.verifyMatchBaseline'(filename, baselineDir, FailureHandling.CONTINUE_ON_FAILURE)

'Compare logo is matched the another image'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ImageComparison.areMatched'(baselineDir + "\\" + filename, screenshotDir + "\\" + filename, FailureHandling.CONTINUE_ON_FAILURE)

'Get difference ratio of two images'
def ratio = CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ImageComparison.getDifferenceRatio'(baselineDir + "\\" + filename, screenshotDir + "\\" + filename, FailureHandling.CONTINUE_ON_FAILURE)
println ratio

prev = ObjectRepository.findTestObject('Object Repository/Cambridge/lnk_Nav_Prev')
next = ObjectRepository.findTestObject('Object Repository/Cambridge/lnk_Nav_Next')
'Take screenshot arrows'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeWebElementsScreenshot'([prev, next], 'arrows.png',
													  10, FailureHandling.OPTIONAL)

imgEnglish = ObjectRepository.findTestObject('Object Repository/Cambridge/img_English_Dic')
iCopyright = ObjectRepository.findTestObject('Object Repository/Cambridge/ita_English_Copyright')
'Take screenshot arrows'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeElementScreenshotIgnoringAreas'(imgEnglish, 'english_dic.png',
													[iCopyright], 10, FailureHandling.OPTIONAL)


"TEARDOWN"
@TearDown
def teardownMethod(){
	'Close the web browser'
	WebUI.closeBrowser()
}