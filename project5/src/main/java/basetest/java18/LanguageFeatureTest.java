package basetest.java18;

import java.net.spi.InetAddressResolver;
import java.net.spi.InetAddressResolverProvider;
import java.util.ServiceLoader;

public class LanguageFeatureTest {
    public static void main(String[] args) {

    }

    // Allows service loader to find address resolver instead of OS resolver
//    private static InetAddressResolver loadResolver() {
//        return ServiceLoader.load(InetAddressResolverProvider.class)
//                            .findFirst()
//                            .map(nsp -> nsp.get(builtinConfiguration()))
//                            .orElse(BUILTIN_RESOLVER);
//    }
}
