# katalon-studio-ashot-plugin

> **Also try out the [early release of Katalon TestOps Vision for Visual-based Testing](https://forum.katalon.com/t/early-release-of-katalon-testops-vision-visual-testing-image-comparison/45557)!**

The plugin provide custom keywords for working with taking screenshots with different options: normal mode, full page of the current web site, take screenshot and scale image according to device pixel ratio or cut off header & footer and take screenshot of web element(s). After that we save the screenshot into file .png files in the folder "Screenshots" of current working project folder. The images can be used as baseline images to detect GUI changes.

## Usage
### Take screenshot normal mode
```groovy
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeScreenshot'('fullscreen.png', FailureHandling.OPTIONAL)
```

### Take screenshot of a full website page
```groovy
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeEntirePageScreenshot'('fullpage_screen.png', FailureHandling.OPTIONAL)
```

### Take screenshot then cut off header and footer
```groovy
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeCuttingScreenshot'('cutting_screen.png', FailureHandling.OPTIONAL)
```

### Take screenshot then scale the image according to device pixel ratio
```groovy
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeScalingScreenshot'('cutting_screen.png', 2,  FailureHandling.OPTIONAL)
```

### Take screenshot of a web element(s)
```groovy
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeWebElementScreenshot'(ObjectRepository.findTestObject('Object Repository/Cambridge/img_Logo'), filename, 10, FailureHandling.OPTIONAL)

prev = ObjectRepository.findTestObject('Object Repository/Cambridge/lnk_Nav_Prev')
next = ObjectRepository.findTestObject('Object Repository/Cambridge/lnk_Nav_Next')
'Take screenshot arrows'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeWebElementsScreenshot'([prev, next], 'arrows.png',
													  10, FailureHandling.OPTIONAL)                            
```
### Take screenshot of a web elements ignoring inside areas
```groovy
imgEnglish = ObjectRepository.findTestObject('Object Repository/Cambridge/img_English_Dic')
iCopyright = ObjectRepository.findTestObject('Object Repository/Cambridge/ita_English_Copyright')
'Take screenshot arrows'
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ScreenCapture.takeElementScreenshotIgnoreAreas'(imgEnglish, 'english_dic.png',
													[iCopyright], 10, FailureHandling.OPTIONAL)
```
The output is image of web element with ingored areas being marked in gray color

![Screenshot of element with ignoring areas](https://github.com/katalon-studio/katalon-studio-ashot-plugin/blob/master/Screenshots/english_dic.png)

### Compare images
```groovy
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ImageComparison.verifyMatchBaseline'(filename, baselineDir, FailureHandling.CONTINUE_ON_FAILURE)

CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ImageComparison.areMatched'(expected, actual, FailureHandling.CONTINUE_ON_FAILURE)
```
If they are not matched, an image will be created with different parts being highlighted

```groovy
CustomKeywords.'kms.turing.katalon.plugins.visualtesting.ImageComparison.getDifferenceRatio'(imageFile1, imageFile2, FailureHandling.CONTINUE_ON_FAILURE)
```

## Companion products

### Katalon TestOps

[Katalon TestOps](https://analytics.katalon.com) is a web-based application that provides dynamic perspectives and an insightful look at your automation testing data. You can leverage your automation testing data by transforming and visualizing your data; analyzing test results; seamlessly integrating with such tools as Katalon Studio and Jira; maximizing the testing capacity with remote execution.

* Read our [documentation](https://docs.katalon.com/katalon-analytics/docs/overview.html).
* Ask a question on [Forum](https://forum.katalon.com/categories/katalon-analytics).
* Request a new feature on [GitHub](CONTRIBUTING.md).
* Vote for [Popular Feature Requests](https://github.com/katalon-analytics/katalon-analytics/issues?q=is%3Aopen+is%3Aissue+label%3Afeature-request+sort%3Areactions-%2B1-desc).
* File a bug in [GitHub Issues](https://github.com/katalon-analytics/katalon-analytics/issues).

### Katalon Studio
[Katalon Studio](https://www.katalon.com) is a free and complete automation testing solution for Web, Mobile, and API testing with modern methodologies (Data-Driven Testing, TDD/BDD, Page Object Model, etc.) as well as advanced integration (JIRA, qTest, Slack, CI, Katalon TestOps, etc.). Learn more about [Katalon Studio features](https://www.katalon.com/features/).
