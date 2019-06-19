package info.labunsky.stego.primitives;

@FunctionalInterface
public interface StegoExtract<State, Element, DataPart> {
    DataPart apply(State st, Element el);
}
