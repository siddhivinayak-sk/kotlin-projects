- Class Data Sharing (CDS) for Application Classes
  java -XX:ArchiveClassesAtExit=<archive filename> -cp <app jar> AppName - create file
  java -XX:SharedArchiveFile=<archive filename> -cp <app jar> AppName - use file
- Z Garbage Collector (ZGC) - Uncommit Unused Memory (JEP 351)
- Reimplement legacy Socket API
- Miscellaneous Updates:
  java.nio – method FileSystems.newFileSystem(Path, Map<String, ?>) added
  java.time – new official Japanese era name added
  javax.crypto – support for MS Cryptography Next Generation (CNG)
  javax.security – property jdk.sasl.disabledMechanisms added to disable SASL mechanisms
  javax.xml.crypto – new String constants introduced to represent Canonical XML 1.1 URIs
  javax.xml.parsers – new methods added to instantiate DOM and SAX factories with namespaces support
  Unicode support upgraded to version 12.1
  Support added for Kerberos principal name canonicalization and cross-realm referrals