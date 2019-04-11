package kms.turing.katalon.plugins.visualtesting

import java.awt.image.BufferedImage
import java.nio.file.Paths
import java.text.DecimalFormat

import javax.imageio.ImageIO

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling

import kms.turing.katalon.plugins.common.BaseKeyword
import kms.turing.katalon.plugins.helper.FileHelper
import ru.yandex.qatools.ashot.comparison.ImageDiff
import ru.yandex.qatools.ashot.comparison.ImageDiffer

public class ImageComparison extends BaseKeyword{
	static final String screenshotDir = RunConfiguration.getProjectDir() + File.separator + "Screenshots"

	/**
	 * Compare an captured image against baseline item
	 * @param filename name of the captured image
	 * @param baselinePath location of the baseline image
	 * @return boolean
	 */
	@Keyword
	static boolean verifyMatchBaseline(String filename, String baselinePath, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		try{
			info "Verify the image with name: $filename is matched to the baseline image in directory: $baselinePath"

			String expectedImgpath = Paths.get(baselinePath).resolve(filename)
			String actualImgPath = Paths.get(screenshotDir).resolve(filename)
			areMatched(expectedImgpath, actualImgPath, null)
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Verify two images are matched or not
	 * @param expectedImgPath the location of expected image
	 * @param actualImgPath the location of actual image
	 * @return
	 */
	@Keyword
	static boolean areMatched(String expectedImgPath, String actualImgPath, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		try{
			info "Verify the image at location: $expectedImgPath is matched to the other image at: $actualImgPath"
			BufferedImage expectedImage = ImageIO.read(new File(expectedImgPath))
			BufferedImage actualImage = ImageIO.read(new File(actualImgPath))

			ImageDiffer imgDiff = new ImageDiffer()
			ImageDiff diff = imgDiff.makeDiff(actualImage, expectedImage)
			if(diff.hasDiff()){
				String filename = new File(actualImgPath).getName()
				String extension = FileHelper.getFileExtension(filename)
				ImageIO.write(diff.getMarkedImage(), extension, Paths.get(screenshotDir).resolve("diff_" + filename).toFile())
				handleError("Actual image does not match to baselined image", flowControl)
			}
		}catch(ex){
			handleError(ex, flowControl)
		}
	}

	/**
	 * Get percentage of differences between two images
	 * @param expected location of the expected image
	 * @param actual location of the expected image
	 * @return double
	 */
	@Keyword
	public static double getDifferenceRatio(String expected, String actual, FailureHandling flowControl = FailureHandling.STOP_ON_FAILURE){
		try{
			info "Get diferences ratio of two images"
			BufferedImage expectedImage = ImageIO.read(new File(expected))
			BufferedImage actualImage = ImageIO.read(new File(actual))

			ImageDiffer imgDiff = new ImageDiffer()
			ImageDiff diff = imgDiff.makeDiff(actualImage, expectedImage)
			int diffSize = diff.getDiffSize()
			int area = diff.getDiffImage().getWidth() * diff.getDiffImage().getHeight()
			double ratio = diffSize/area * 100
			return ratio
		}catch(ex){
			handleError(ex, flowControl)
		}
	}
}
