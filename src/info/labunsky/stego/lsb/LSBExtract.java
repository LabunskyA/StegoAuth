package info.labunsky.stego.lsb;

import info.labunsky.stego.primitives.stateless.StegoExtractStateless;

public class LSBExtract implements StegoExtractStateless<Integer, Integer> {
    @Override
    public Integer apply(Integer el) {
        return el & 1;
    }
}
