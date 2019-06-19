package info.labunsky.stego.auth;

import info.labunsky.stego.lsb.LSBEmbed;
import info.labunsky.stego.lsb.LSBExtract;
import info.labunsky.stego.lsb.pixels.NGPixels;
import info.labunsky.stego.primitives.stateless.StegoMachineStateless;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpyUtil {
    private static void usage() {
        System.out.println("-e [container] - execute sequence from stdin");
        System.out.println("-ch [hash path] [message] - check message hash");
    }

    private static boolean checkHash(String message, byte[] digest) {
        return Arrays.equals(digest, Commons.hash(message));
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            usage();
            return;
        }

        switch (args[0]) {
            case "-e": {
                final File containerFile = new File(args[1]);

                final StegoMachineStateless<Integer> machine = new StegoMachineStateless<>(new NGPixels(containerFile));

                final LSBExtract ex = new LSBExtract();
                final LSBEmbed em = new LSBEmbed();

                final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String line;

                byte b = 0; short sh = 0;
                final List<Byte> bytes = new ArrayList<>();

                while ((line = br.readLine()) != null) {
                    final String[] tokens = line.split(":");
                    if (tokens.length == 1) {
                        b |= machine.exec(Integer.parseInt(tokens[0]), ex) << sh;
                        if (++sh == 8) {
                            bytes.add(b);
                            sh = 0; b = 0;
                        }
                    } else if (tokens.length == 2)
                        machine.exec(Integer.parseInt(tokens[0]), em, Integer.parseInt(tokens[1]));
                    else
                        throw new IOException("Wrong input format: " + line);
                }

                if (bytes.size() > 0) {
                    final byte[] temp = new byte[bytes.size()];
                    for (int i = 0; i < temp.length; i++)
                        temp[i] = bytes.get(i);
                    System.out.println(new String(temp));
                }

                final NGPixels container = (NGPixels) machine.getContainer();
                container.saveAs("PNG", containerFile);
            } break;

            case "-c":
                System.out.print(args[2]);
                if (checkHash(args[2], Files.readAllBytes(Paths.get(args[1]))))
                    System.out.println(" - Correct");
                else
                    System.out.println(" - Incorrect");
                break;

            default:
                usage();
        }
    }
}
