package sobel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sobel {
    
    public double[][] Magnitute;
    public double[][] Direction;

    /*----------------------------------------------------------------------------------------------
     TODO: Needs to be scaled 
     ----------------------------------------------------------------------------------------------*/
    public void process(String filename) {

        int[][] img = ImageRead(filename);
        int rows = img.length;
        int cols = img[0].length;

        double[][] Gx = new double[rows][cols];
        double[][] Gy = new double[rows][cols];
        double[][] Mag = new double[rows][cols];
        double[][] Dir = new double[rows][cols];

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {

                    Gx[i][j] = Gy[i][j] = Mag[i][j] = 0; // Initialize

                } else {

                    Gx[i][j] = img[i + 1][j - 1] + 2 * img[i + 1][j] + img[i + 1][j + 1]
                            - img[i - 1][j - 1] - 2 * img[i - 1][j] - img[i - 1][j + 1];

                    Gy[i][j] = img[i - 1][j + 1] + 2 * img[i][j + 1] + img[i + 1][j + 1]
                            - img[i - 1][j - 1] - 2 * img[i][j - 1] - img[i + 1][j - 1];

                    //--- Compute Magnitute and Direction ---//
                    Mag[i][j] = Math.sqrt(Gx[i][j] * Gx[i][j] + Gy[i][j] * Gy[i][j]); // Magnitude

                    Dir[i][j] = Math.atan2(Gy[i][j], Gx[i][j]);                      // Direction

                }

            } /*--- for (int j = 0; j < ncols; j++) ---*/

        } /*--- for (int i = 0; i < nrows; i++) ---*/

        Magnitute = ScaleMagnitude(Mag);
        
        Direction = ScaledDirection(Dir);

    }
    /*--------------------------------------------------------------------------------------------
      Scale Magnitute result prior to writting to a file     
      sqrt ( pow2( 1024 ) + pow2(1024) ) = 1448.154687
    /*--------------------------------------------------------------------------------------------*/

    private double[][] ScaleMagnitude(double[][] Mag) {
        
        double[][] mag = new double[Mag.length][Mag[0].length];
        for (int i = 0; i < Mag.length; i++) {
            for (int j = 0; j < Mag[i].length; j++) {
                    mag[i][j] = (Mag[i][j] * 255) / 1448.154687;
            }
        }
        return mag;
    }
    /*--------------------------------------------------------------------------------------------
      Scale Direction result prior to writting to a file             
      pi = 3.1415926
      2 * pi = 6.2831853
    /*--------------------------------------------------------------------------------------------*/

    private double[][] ScaledDirection(double[][] Dir) {
        
        double[][] dir = new double[Dir.length][Dir[0].length];
        for (int i = 0; i < Dir.length; i++) {
            for (int j = 0; j < Dir[i].length; j++) {
                    dir[i][j] = ((Dir[i][j] + 3.1415926) / 6.2831853) * 255;
            }
        }
        return dir;
    }
    
    /*--------------------------------------------------------------------------------------------*/

    private int[][] ImageRead(String filename) {

        try {
            File infile = new File(filename);
            BufferedImage bi = ImageIO.read(infile);

            int red[][] = new int[bi.getHeight()][bi.getWidth()];
            int grn[][] = new int[bi.getHeight()][bi.getWidth()];
            int blu[][] = new int[bi.getHeight()][bi.getWidth()];
            for (int i = 0; i < red.length; ++i) {
                for (int j = 0; j < red[i].length; ++j) {
                    red[i][j] = bi.getRGB(j, i) >> 16 & 0xFF;
                    grn[i][j] = bi.getRGB(j, i) >> 8 & 0xFF;
                    blu[i][j] = bi.getRGB(j, i) & 0xFF;
                }
            }

            return grn;

        } catch (IOException e) {
            System.out.println("image I/O error");
            return null;
        }
    }
    /*--------------------------------------------------------------------------------------------*/

    private void ImageWrite(String filename, double img[][]) {
        try {
            BufferedImage bi = new BufferedImage(img[0].length, img.length, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < bi.getHeight(); ++i) {
                for (int j = 0; j < bi.getWidth(); ++j) {
                    int val = (int) img[i][j];
                    int pixel = (val << 16) | (val << 8) | (val);
                    bi.setRGB(j, i, pixel);
                }
            }

            File outputfile = new File(filename);
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    /*--------------------------------------------------------------------------------------------*/
}
