package info.labunsky.stego.primitives.stateless;

@FunctionalInterface
public interface StegoEmbedStateless<Element, DataPart> {
    Element apply(Element el, DataPart p);
}
