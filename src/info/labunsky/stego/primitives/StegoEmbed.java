package info.labunsky.stego.primitives;

@FunctionalInterface
public interface StegoEmbed<State, Element, DataPart> {
    Element apply(State st, Element el, DataPart p);
}
