package info.labunsky.stego.primitives.stateless;

import info.labunsky.stego.primitives.StegoContainer;

public class StegoMachineStateless<Element> {
    private final StegoContainer<Element> container;

    public StegoMachineStateless(StegoContainer<Element> container) {
        this.container = container;
    }

    public <DataPart> void exec(int i, StegoEmbedStateless<Element, DataPart> f, DataPart p) {
        container.set(i, f.apply(container.get(i), p));
    }

    public <DataPart> DataPart exec(int i, StegoExtractStateless<Element, DataPart> f) {
        return f.apply(container.get(i));
    }

    public StegoContainer<Element> getContainer() {
        return container;
    }
}
