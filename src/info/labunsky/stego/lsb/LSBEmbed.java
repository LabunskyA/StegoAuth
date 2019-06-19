package info.labunsky.stego.lsb;

import info.labunsky.stego.primitives.stateless.StegoEmbedStateless;

public class LSBEmbed implements StegoEmbedStateless<Integer, Integer> {
    @Override
    public Integer apply(Integer el, Integer bit) {
        return (el & ~1) | bit;
    }
}
