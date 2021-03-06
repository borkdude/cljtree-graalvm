
# cljtree-graalvm

A Clojure version of `tree` built with GraalVM.

## Credits

This repo is inspired by:

- https://gitlab.com/konrad.mrozek/cljfmt-graalvm
- https://github.com/lambdaisland/birch

## Usage

![Usage](cljtree.gif)

## Options

- `--color` or `-c`: colorize output
- `--edn` or `-E`: output EDN

The path argument is optional and will default to the current directory.

## Build

- Install [lein](https://leiningen.org/)
- Download [GraalVM](http://www.graalvm.org/downloads/) for your machine. You will need the EE version if you're using MacOS.
- Set `JAVA_HOME` to the GraalVM home directory, e.g.

```sh
export JAVA_HOME=~/Downloads/graalvm-1.0.0-rc1/Contents/Home
```

- Set the `PATH` to use GraalVM's binaries, e.g.

```sh
export PATH=$PATH:~/Downloads/graalvm-1.0.0-rc1/Contents/Home/bin
```

- Create the uberjar:

```sh
lein uberjar
```

- Finally, create the binary:

``` sh
native-image -jar target/cljtree-graalvm-0.1.0-SNAPSHOT-standalone.jar -H:Name="cljtree" -H:+ReportUnsupportedElementsAtRuntime
```
