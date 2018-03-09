package com.enablix.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import net.coobird.thumbnailator.Thumbnails;

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
        String in = "D:/enablix/filestorage/enablixmaster//tmp/preview/f1c05d18-14bb-4cd7-91b1-30cd391db67c-1.png";
        BufferedImage inImg = ImageIO.read(Files.newInputStream(Paths.get(in)));
		BufferedImage bi = createThumb(inImg, 320, 320);
        String ext = in.substring(in.lastIndexOf(".")+1);
        String out = in.replaceFirst(".([a-z]+)$", "_thumb." + ext);
        String out1 = in.replaceFirst(".([a-z]+)$", "_thumb1." + ext);
        String out2 = in.replaceFirst(".([a-z]+)$", "_thumb2." + ext);
        System.err.println(in + " --> " + out);
        BufferedImage outImg = Scalr.resize(bi, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, 320, 320);
        Thumbnails.of(inImg).size(240, 240).toFile(out2);
        ImageIO.write(bi, ext, Files.newOutputStream(Paths.get(out)));
        ImageIO.write(outImg, ext, Files.newOutputStream(Paths.get(out1)));
    }
	
}
