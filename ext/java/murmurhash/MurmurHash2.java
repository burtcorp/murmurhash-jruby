package murmurhash;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.RubyFixnum;
import org.jruby.RubyString;
import org.jruby.RubyBignum;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.util.ByteList;
import org.jruby.util.MurmurHash;

@JRubyClass(name="MurmurHash::MurmurHash2")
public class MurmurHash2 extends RubyObject {
  private static int DEFAULT_SEED = 754110351;

  private int seed = DEFAULT_SEED;

  public static RubyClass createRubyClass(Ruby runtime, RubyModule parentModule) {
    RubyClass klass = runtime.defineClassUnder("MurmurHash2", runtime.getObject(), ALLOCATOR, parentModule);
    klass.defineAnnotatedMethods(MurmurHash2.class);
    return klass;
  }

  private static final ObjectAllocator ALLOCATOR = new ObjectAllocator() {
    @Override
    public IRubyObject allocate(Ruby runtime, RubyClass klass) {
      return new MurmurHash2(runtime, klass);
    }
  };

  public MurmurHash2(Ruby runtime, RubyClass type) {
    super(runtime, type);
  }

  @JRubyMethod(optional = 1)
  public IRubyObject initialize(final ThreadContext ctx, final IRubyObject[] args) {
    if (args.length > 0 && !args[0].isNil()) {
      this.seed = (int) args[0].convertToInteger().getLongValue();
    }
    return this;
  }

  @JRubyMethod(required = 1)
  public IRubyObject hash32(final ThreadContext ctx, final IRubyObject arg) {
    return _hash32(ctx.runtime, arg.convertToString(), seed);
  }

  @JRubyMethod(module = true, required = 1, optional = 1)
  public static IRubyObject hash32(final ThreadContext ctx, final IRubyObject recv, final IRubyObject[] args) {
    int seed = DEFAULT_SEED;
    if (args.length > 1 && !args[1].isNil()) {
      seed = (int) args[1].convertToInteger().getLongValue();
    }
    return _hash32(ctx.runtime, args[0].convertToString(), seed);
  }

  private static IRubyObject _hash32(final Ruby runtime, final RubyString input, final int seed) {
    final ByteList bytes = input.getByteList();
    final int result = MurmurHash.hash32(bytes.unsafeBytes(), bytes.begin(), bytes.length(), seed);
    return RubyFixnum.newFixnum(runtime, result);
  }
}
