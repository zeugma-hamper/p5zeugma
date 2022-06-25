# p5zeugma
## aka zeugma-processing

The **p5zeugma** library collects a set of classes that yoke
**Processing** to **Zeugma**, so that the former becomes the renderer
for the (Java version of the) latter. The library is thus the jvm-land
exemplar -- well, first example, anyway -- of Zeugma's offer of a
renderer-ecumenical core that docks with a variety of rendering "back
ends".

In the present instance, the inveterate Zeugma programmer can of
course code via Zeugma's idioms: scene graphs and the object-oriented
representation of graphical, uh, objects that populate them; spatial,
multi-user event structures and plumbing for routing same; metrically
accurate coordinate system (i.e. room coordinates) in which each
display and input device is embedded; etc. However, an additional
facility allows Processing-native folk to continue to work more or
less without change of habit (or knowledge of Zeugma), simply
requiring that drawing code be placed in `PZDraw ()` rather than in
`draw ()` -- p5zeugma slyly sets up one of its own drawing layers with
a set of transformations that undo three-spaceness and return
representation to 2d x & y (inverted) coordinates, and then has
`PZDraw ()` execute its code in that context.


### building the thing via Gradle

Prerequisites for the build are

1. having yourself built, or more specifically in one way or other having
mavenish access to, the `zeugma-core` jar. Please see the README stapled to
the front of the _https://github.com/java-zeugma.git_ repository for
instructions on building `zeugma-core` from scratch.

2. having built, from a directory that must be situated parallel to
the top-level directory of this project, **Processing**. You'd
probably want to clone it from
_https://github.com/processing/processing4_; note please that best
results at the moment obtain from the beta-8 release of Processing
4.0, accessible via the tag `processing-1283-4.0b8`.  To perform the
build you'll simply invoke **ant** from the project's `build`
directory.

Two distinct jars can be emitted from the build process. One, with a
name that'll be something like
`zeugma-processing-0.0.05-SNAPSHOT.jar`, is a straightforward
a-la-carte library containing only the code from **p5zeugma** (well,
also the platform-specific native libraries that connect Processing to
the actual business end of OpenGL rendering). The other is an uberjar
that also includes the whole of the **zeugma-core** codebase, as well
as necessary **OSC** and **JSON** libraries. This latter will be named
with `-all` affixed to the basename above, and is is presumed to be
the more convenient way to use **Zeugma** (versus 'linking'
individually against the various constituent jars).

And so now to the build proper. If you're looking for jars to pick up and
toss gleefully around, then
```sh
./gradlew jar
```
will produce the a-la-carte jar -- depositing it in `build/libs/` -- while
```sh
./gradlew shadowJar
```
will net you the uberjar version in the same location.

Most likely what you'll intend, however, is
```sh
./gradlew publishToMavenLocal
```
by which you 'install' both jar versions into your local `~/.m2` maven
pseudorepository.

- - -

### other notes

(a) At the moment, **p5zeugma** will work on macOS running on an m1
processor but not on an Intel processor. This is because something in
Processing's runtime dynamic loader searches for native libraries
anachronistically in `macosx-universal` rather than in the proper
platform specific location (i.e. one of `macos-aarch64` and
`macos-x86-64`). The present **p5zeugma** build system accordingly
copies the m1 libraries from `macos-aarch64` into a placatory
directory called `macosx-universal`. We expect the causal troublesome
behavior to be corrected upstream in some future Processing release...

(b) When any **p5zeugma** program starts, it tries to load two
configuration files (`maes-config.json` specifies the physical layout
of display surfaces in space, while `coord-xform-raw-to-room.json`,
specifies the transformation needed to convert raw spatial input
device coordinates to room coordinates). The first attempt is to
locate 'proximal' versions of the files in a directory called `config`
in the project directory. For each file that can't be found there, an
attempt to load from a 'system' location at `/opt/trelopro/config`
ensues. At the moment, p5zeugma programs will fail to start (via the
dull thud of a null pointer exception) if either file cannot be found
in one of those locations. This is self-evidently a terrible state of
affairs, and will shortly be corrected (reasonable defaults will be
used if the config files are absent). In any event, we recommend that
you copy the `config` directory supplied in this repository into
`/opt/trelopro/`.
