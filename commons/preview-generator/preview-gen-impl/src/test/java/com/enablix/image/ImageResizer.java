package com.enablix.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImageResizer {

	public static BufferedImage createThumb(BufferedImage in, int w, int h) {
        // scale w, h to keep aspect constant
        double outputAspect = 1.0*w/h;
        double inputAspect = 1.0*in.getWidth()/in.getHeight();
        if (outputAspect < inputAspect) {
            // width is limiting factor; adjust height to keep aspect
            h = (int)(w/inputAspect);
        } else {
            // height is limiting factor; adjust width to keep aspect
            w = (int)(h*inputAspect);
        }
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(in, 0, 0, w, h, null);
        g2.dispose();
        return bi;
    }

    public static void main(String args[]) throws Exception {
        for (String in : args) {
            BufferedImage inImg = ImageIO.read(Files.newInputStream(Paths.get(in)));
			BufferedImage bi = createThumb(
                inImg,
                450, 450);
            String ext = in.substring(in.lastIndexOf(".")+1);
            String out = in.replaceFirst(".([a-z]+)$", "_thumb." + ext);
            String out1 = in.replaceFirst(".([a-z]+)$", "_thumb1." + ext);
            System.err.println(in + " --> " + out);
            BufferedImage outImg = Scalr.resize(bi, 300, 300);
            ImageIO.write(bi, ext, Files.newOutputStream(Paths.get(out)));
            ImageIO.write(outImg, ext, Files.newOutputStream(Paths.get(out1)));
        }
    }
	
}
