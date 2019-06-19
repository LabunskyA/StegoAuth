package info.labunsky.stego.primitives;

public class StegoMachine<State, Element> {
    private final State state;
    private final StegoContainer<Element> container;

    public StegoMachine(StegoContainer<Element> container) {
        this(null, container);
    }

    public StegoMachine(State initial_state, StegoContainer<Element> container) {
        this.state = initial_state;
        this.container = container;
    }

    public <DataPart> void exec(int i, StegoEmbed<State, Element, DataPart> f, DataPart p) {
        container.set(i, f.apply(state, container.get(i), p));
    }

    public <DataPart> DataPart exec(int i, StegoExtract<State, Element, DataPart> f) {
        return f.apply(state, container.get(i));
    }

    public State getState() {
        return state;
    }

    public StegoContainer<Element> getContainer() {
        return container;
    }
}