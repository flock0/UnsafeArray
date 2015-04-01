UnsafeArray
===========

An array-like implementation of objects stored off-heap.

Installation
------------

Use `mvn package` to compile. For the maven tests to succeed on 64-bit JVMs, use `mvn package -P x64`

Known Issues
------------

Compressed ordinary object pointers are not supported at this point. 64-bit JVMs should therefore be started with the `-XX:-UseCompressedOops` option.
