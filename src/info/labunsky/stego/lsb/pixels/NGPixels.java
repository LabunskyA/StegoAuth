package info.labunsky.stego.lsb.pixels;

import info.labunsky.stego.primitives.StegoContainer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NGPixels implements StegoContainer<Integer> {
    private final static class Pixel {
        private final int x;
        private final int y;
        private int value;

        private Pixel(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    private final BufferedImage image;
    private final List<Pixel> rgb;

    public NGPixels(final File file) throws IOException {
        image = ImageIO.read(file);
        rgb = new ArrayList<>(image.getWidth() * image.getHeight() / 5);

        for (int y = 0; y < image.getHeight(); y++)
            for (int x = 0; x < image.getHeight(); x++) {
                final int target = image.getRGB(x, y) & 0x00fefefe;

                boolean suitable = target != 0;
                for (int ty = Math.max(0, y-1); ty <= Math.min(image.getHeight()-1, y+1) && suitable; ty++)
                    for (int tx = Math.max(0, x-1); tx <= Math.min(image.getWidth()-1, x+1) && suitable; tx++) {
                        if (tx == x && ty == y)
                            continue;

                        final int local = image.getRGB(tx, ty) & 0x00fefefe;
                        if (local == target)
                            suitable = false;
                    }

                if (suitable) {
                    rgb.add(new Pixel(x, y, (image.getRGB(x, y) & 0x00ff0000) >> 16));
                    rgb.add(new Pixel(x, y, (image.getRGB(x, y) & 0x0000ff00) >> 8));
                    rgb.add(new Pixel(x, y, (image.getRGB(x, y) & 0x000000ff)));
                }
            }
    }

    public void saveAs(String format, File dest) throws IOException {
        for (int i = 0; i < rgb.size(); i += 3) {
            final int alpha = image.getRGB(rgb.get(i).x, rgb.get(i).y) & 0xff000000;

            image.setRGB(
                    rgb.get(i).x, rgb.get(i).y,
                    alpha | (rgb.get(i).value << 16) | (rgb.get(i+1).value << 8) | (rgb.get(i+2).value)
            );
        }

        ImageIO.write(image, format, dest);
    }

    @Override
    public Integer get(int i) {
        return rgb.get(i).value;
    }

    @Override
    public void set(int i, Integer value) {
        rgb.get(i).value = value;
    }

    @Override
    public int size() {
        return rgb.size();
    }
}
