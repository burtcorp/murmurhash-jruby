package murmurhash;

import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.runtime.load.Library;

public class MurmurHashLibrary implements Library {
  public void load(Ruby runtime, boolean wrap) {
    RubyModule module = runtime.defineModule("MurmurHash");
    MurmurHash2.createRubyClass(runtime, module);
    MurmurHash3.createRubyClass(runtime, module);
  }
}
