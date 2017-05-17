package com.enablix.doc.converter.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFToImage {

	public static void main(String[] args) throws IOException {
		
		String PDFFILE = "C:\\Users\\dluthra\\Downloads\\Invoice-RI-1320.pdf";
		String outFolder = "C:\\Users\\dluthra\\Downloads\\preview\\";
		String imageFilePrefix = "Invoice-RI-1320";
		
		PDDocument document = PDDocument.load(new File(PDFFILE));

        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); ++page) {

            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            FileOutputStream out = new FileOutputStream(outFolder + imageFilePrefix + "-" + (page + 1) + ".png");
            ImageIO.write(bim, "png", out);

        }
        document.close();

		
	}
	
}
