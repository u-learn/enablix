package com.enablix.doc.converter.ppt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.TextParagraph;

import com.enablix.commons.util.IOUtil;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocumentStream;

public class PowerpointToImageConverter {

	public PowerpointToImageConverter() {
		super();
	}

	protected <S extends Shape<S,P>, P extends TextParagraph<S,P,?>> 
			void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, 
					DocumentFormat outFormat, String imageFormatName, SlideShowCreator<S, P> ssCreator) throws IOException {
	
		try (SlideShow<S, P> ppt = ssCreator.createSlideShow(is)) {
	
			double zoom = 1; // magnify it by 2
			AffineTransform at = new AffineTransform();
			at.setToScale(zoom, zoom);
	
			Dimension pgsize = ppt.getPageSize();
	
			List<? extends Slide<S, P>> slides = ppt.getSlides();
			
			os.startDocumentWriting();
	
			for (Slide<S, P> slide : slides) {
				
				BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width * zoom),
						(int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
				
				Graphics2D graphics = img.createGraphics();
				// graphics.setTransform(at);
	
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
				
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
				slide.draw(graphics);
	
				OutputStream out = os.nextPageOutputStream();
				
				try {
					
					os.startPageWriting();
					
					ImageIO.write(img, imageFormatName, out);
					
					os.endPageWriting(IOUtil.getImageProperties(img));
					
				} catch (Exception e) {
					
					os.writingError(e);
					throw e;
					
				} finally {
					graphics.dispose();
					img.flush();	
				}
			}
			
			os.endDocumentWriting(new HashMap<String, Object>());
		}
	}
	
	protected interface SlideShowCreator<S extends Shape<S,P>, P extends TextParagraph<S,P,?>> {
		
		SlideShow<S, P> createSlideShow(InputStream is) throws IOException;
		
	}
	
}