# StegoAuth
Set of utilities and an API as of proof the concept for asymmetric steganographic authentication written in Java.

## Building
Artifacts available at the [releases](https://github.com/LabunskyA/StegoAuth/releases) page, but one can also build everything with a simple `make`. Note that you will need to have JDK 8+ in your PATH variable.

## Usage
### The protocol
Let Trent be the headquarters, Alice and Bob - agents. The authentication protocol divided into two phases.

Generating authintification information beforehand:
- T: chooses a secret algorithm and a key, with them generates:
  - an extract sequence *Ex*;
  - a suitable authentication context *Ctx*;
- T -> A: *Ctx, Ex*;
- T: using *Ex* and the created context:
  - a message *M* with *|Ex|* parts in it;
  - an open one-time *Em* sequence by shuffling indices within *Ex*;
- T -> B: *Em, h(M)*.

Agent authentication works like this:
- A -> B: an initializing message *IM* based on *Ctx* with a container description in it;
- B: chooses suitable *C ~ IM*;
- B -> A: *C' = Em(C)*;
- A: checks if *C' ~ IM*;
- A -> B: *M' = Ex(C')*, mark M' as used;
- B: checks if *h(M') == h(M)*, destroys *Em, h(M)*.

### API
To create your own steganography authentification method for the protocol you will need to implement a couple of interfaces:
~~~java
class MyContainer implements StegoContainer<MyElement> {
    public MyElement get(int i) {
        // ...
    }
    public void set(int i, MyElement v) {
        // ...
    }
    public int size() {
        // elements count
    }
}

final StegoEmbed myEmbed = (st, el, dp) -> {
    // embed data part dp in element el with the state st
};

final StegoExtract myExtract = (st, el) -> {
   // extract data part dp from el with the state st
   return dp;
};
~~~

And use them with a generic `StegoMachine`:
~~~java
StegoMachine<MyState, MyElement> myMachine = new StegoMachine(
    initialState, new MyContainer<MyElement>(/* loading container */)
);

// Embedding message parts
MyDataPart part = /* create data element */;
myMachine.exec(1337, myEmbed, part);
// Repeat...

// Getting and changing the state if we want to / need to
State currState = myMachine.getState();
// ...

// extracting data parts
Part = myMachine.exec(80085, myExtract);
// repeat...

// getting container to save or whatever
MyContainer container = (Mycontainer) myMachine.getContainer();
container.save(new File("my_container"));
~~~

### Command-line tools
Use HQUtil to generate auth info for any stateless method to use.
Just call it with these flags:
~~~
-ex [message size in bytes] [indices bound] - generate extract sequence
-em [message] OPTIONAL: [trash/message bits ratio] [indices bound] - generate embed sequence from Ex in stdin
-h - calculate hash from stdin
~~~ 

SpyUtil is used to work with generated sequences in the field.
Available flags are:
~~~
-e [container] - execute sequence from stdin
-ch [hash path] [message] - check message hash
~~~

## License
[Simplified BSD](LICENCE)
