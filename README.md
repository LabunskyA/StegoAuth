# StegoAuth
Set of utilities and API to proof the concept of asymmetric steganographic authentication, written in Java. I will link more info in the nearest future.

## Building
Artifacts available at the [releases](https://github.com/LabunskyA/StegoAuth/releases) page, but you can also build everything with simple `make`. Note that you will need to have JDK in your PATH variable.

## Usage
### The protocol
Let Trent be the headquarters, Alice and Bob - agents. Authentication protocol consists of two phases.

Generating auth info beforehand:
- T: chooses secret algorithm and a key, with them generates:
  - extract sequence *Ex*;
  - suitable authentication context *Ctx*;
- T -> A: *Ctx, Ex*;
- T: using *Ex* and created context:
  - message *M* with *|Ex|* parts in it;
  - one-time *Em*, open sequence, shuffling indices from *Ex*;
- T -> B: *Em, h(M)*.

Agent authentication if the field works like this:
- A -> B: initializing message *IM* based on *Ctx* with container description in it;
- B: chooses suitable *C ~ IM*;
- B -> A: *C' = Em(C)*;
- A: checks if *C' ~ IM*;
- A -> B: *M' = Ex(C')*, mark M' as used;
- B: checks if *h(M') == h(M)*, destroying *Em, h(M)*.

### API
To create your own steganography authentification method for the protocol, you will need to implement a couple of interfaces:
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

And use them with generic `StegoMachine`:
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

### CMD-tools
You can use HQUtil to generate auth info for any stateless method to use.
Just use with these flags:
~~~
-ex [message size in bytes] [indices bound] - generate extract sequence
-em [message] OPTIONAL: [trash/message bits ratio] [indices bound] - generate embed sequence from Ex in stdin
-h - calculate hash from stdin
~~~ 

SpyUtil used to use generated sequences in the field.
Available flags are:
~~~
-e [container] - execute sequence from stdin
-ch [hash path] [message] - check message hash
~~~

## License
[Simplified BSD](LICENCE)
