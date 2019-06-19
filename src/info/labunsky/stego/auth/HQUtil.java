package info.labunsky.stego.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.*;

public class HQUtil {
    private static void usage() {
        System.out.println("Available flags are:");

        System.out.println("-ex [message size in bytes] [indices bound] - generate extract sequence");
        System.out.println("-em [message] OPTIONAL: [trash/message bits ratio] [indices bound] - generate embed sequence from Ex in stdin");
        System.out.println("-h - calculate hash from stdin");
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0 || !args[0].contains("-")) {
            usage();
            return;
        }

        switch (args[0].toLowerCase()) {
            default:
            case "-h": {
                final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                final StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null)
                    sb.append(line).append("\n");
                System.out.write(Commons.hash(sb.substring(0, sb.length()-1)));
            } break;

            case "-em": {
                final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                final byte[] message = args[1].getBytes();

                final int bitSize = message.length * 8;

                final Set<Integer> used = new HashSet<>(message.length * 8);
                final List<String> em = new ArrayList<>(message.length * 8);

                int bound = 0;
                for (int i = 0; i < bitSize; i++) {
                    final String exLine = br.readLine();
                    if (exLine == null)
                        throw new IOException("Sequence is not long enough");

                    final int pos = Integer.parseInt(exLine);
                    used.add(pos);
                    bound = Math.max(pos, bound);

                    em.add(String.format("%s:%d", exLine, (message[i/8] & (1 << (i%8))) > 0 ? 1 : 0));
                }

                final SecureRandom sr = new SecureRandom();
                if (args.length > 2) {
                    final double tr = Double.parseDouble(args[2]);
                    if (args.length > 3)
                        bound = Integer.parseInt(args[3]);

                    for (int i = 0; i < tr * bitSize; i++) {
                        final int bit = sr.nextInt(1);

                        int pos;
                        do {
                            pos = sr.nextInt(bound);
                        } while (used.contains(pos));

                        em.add(String.format("%d:%d", pos, bit));
                    }
                }

                Collections.shuffle(em, sr);
                for (String cmd : em)
                    System.out.println(cmd);
            } break;

            case "-ex": {
                int size = Integer.parseInt(args[1]) * 8;
                final int bound = Integer.parseInt(args[2]);

                final SecureRandom sr = new SecureRandom();
                final Set<Integer> used = new HashSet<>(size);

                while (--size >= 0) {
                    int pos;
                    do { pos = sr.nextInt(bound); } while (used.contains(pos));

                    used.add(pos);
                    System.out.println(pos);
                }
            } break;
        }
    }
}
