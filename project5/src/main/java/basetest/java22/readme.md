- Finalization of Foreign Function and Memory API
- Class File API - Read, parse, transform Java's .class files
- Stream Gatherers - allows us to create a custom intermediate operation on Stream; chaining the gather() method on a stream and supplying it with a Gatherer, which is an instance of the java.util.stream.Gatherer interface
  Built-in gatherers:
  fold
  mapConcurrent
  scan
  windowFixed
  windowSliding
- Structured Concurrency (Preview)
- Scoped Values - Scoped values enable storing and sharing immutable data within and across threads. Scoped values introduce a new type, ScopedValue<>
- Vector API (Incubator)
- Multi-File Source Programs (Preview) - Allows to run 'java' programs without 'javac' for multi-file source code files
- G1 Garbage Collector performance Improvements
