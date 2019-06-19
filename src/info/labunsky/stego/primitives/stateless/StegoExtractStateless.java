package info.labunsky.stego.primitives.stateless;

@FunctionalInterface
public interface StegoExtractStateless<Element, DataPart> {
    DataPart apply(Element element);
}
