import java.util.*;
/**
 * Entry point to auto-generated tests (generated by maven-hpi-plugin).
 * If this fails to compile, you are probably using Hudson &lt; 1.327. If so, disable
 * this code generation by configuring maven-hpi-plugin to &lt;disabledTestInjection>true&lt;/disabledTestInjection>.
 */
public class InjectedTest extends junit.framework.TestCase {
  public static junit.framework.Test suite() throws Exception {
    Map parameters = new HashMap();
    parameters.put("basedir","D:\\TestPlugin\\Demo_plugin_Jenkins");
    parameters.put("artifactId","Demo_plugin_Jenkins");
    parameters.put("outputDirectory","D:\\TestPlugin\\Demo_plugin_Jenkins\\target\\classes");
    parameters.put("testOutputDirectory","D:\\TestPlugin\\Demo_plugin_Jenkins\\target\\test-classes");
    parameters.put("requirePI","true");
    return new org.jvnet.hudson.test.PluginAutomaticTestBuilder().build(parameters);
  }
}
