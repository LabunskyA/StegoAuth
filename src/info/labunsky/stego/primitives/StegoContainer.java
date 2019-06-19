package info.labunsky.stego.primitives;

public interface StegoContainer<Element> {
    Element get(int i);
    void set(int i, Element v);
    int size();
}
