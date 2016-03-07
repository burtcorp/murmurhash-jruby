package murmurhash;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.RubyFixnum;
import org.jruby.RubyString;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.util.ByteList;

@JRubyClass(name="MurmurHash::MurmurHash3")
public class MurmurHash3 extends RubyObject {
  private static int DEFAULT_SEED = 1923944541;

  public static RubyClass createRubyClass(Ruby runtime, RubyModule parentModule) {
    RubyClass klass = runtime.defineClassUnder("MurmurHash3", runtime.getObject(), ALLOCATOR, parentModule);
    klass.defineAnnotatedMethods(MurmurHash3.class);
    return klass;
  }

  private static final ObjectAllocator ALLOCATOR = new ObjectAllocator() {
    @Override
    public IRubyObject allocate(Ruby runtime, RubyClass klass) {
      return new MurmurHash3(runtime, klass);
    }
  };

  public MurmurHash3(Ruby runtime, RubyClass type) {
    super(runtime, type);
  }

  @JRubyMethod(required = 1, optional = 1)
  public IRubyObject hash64(final ThreadContext ctx, final IRubyObject[] args) {
    final RubyString input = args[0].convertToString();
    final ByteList bytes = input.getByteList();
    final LongPair result = new LongPair();
    int seed = DEFAULT_SEED;
    if (args.length > 1 && args[1] != null && !args[1].isNil()) {
      seed = (int) args[1].convertToInteger().getLongValue();
    }
    murmurhash3_x64_128(bytes.unsafeBytes(), bytes.begin(), bytes.length(), seed, result);
    return RubyFixnum.newFixnum(ctx.runtime, result.val1);
  }

  /*
   *  The MurmurHash3 algorithm was created by Austin Appleby and placed in the public domain.
   *  This java port was authored by Yonik Seeley and also placed into the public domain.
   *  The author hereby disclaims copyright to this source code.
   *  <p>
   *  This produces exactly the same hash values as the final C++
   *  version of MurmurHash3 and is thus suitable for producing the same hash values across
   *  platforms.
   *  <p>
   *  The 32 bit x86 version of this hash should be the fastest variant for relatively short keys like ids.
   *  murmurhash3_x64_128 is a good choice for longer strings or if you need more than 32 bits of hash.
   *  <p>
   *  Note - The x86 and x64 versions do _not_ produce the same results, as the
   *  algorithms are optimized for their respective platforms.
   *  <p>
   *  See http://github.com/yonik/java_util for future updates to this file.
   */

  /** 128 bits of state */
  private static final class LongPair {
    public long val1;
    public long val2;
  }

  private int fmix32(int h) {
    h ^= h >>> 16;
    h *= 0x85ebca6b;
    h ^= h >>> 13;
    h *= 0xc2b2ae35;
    h ^= h >>> 16;
    return h;
  }

  private long fmix64(long k) {
    k ^= k >>> 33;
    k *= 0xff51afd7ed558ccdL;
    k ^= k >>> 33;
    k *= 0xc4ceb9fe1a85ec53L;
    k ^= k >>> 33;
    return k;
  }

  /** Gets a long from a byte buffer in little endian byte order. */
  private long getLongLittleEndian(byte[] buf, int offset) {
    return ((long)buf[offset+7]          << 56) // no mask needed
              | ((buf[offset+6] & 0xffL) << 48)
              | ((buf[offset+5] & 0xffL) << 40)
              | ((buf[offset+4] & 0xffL) << 32)
              | ((buf[offset+3] & 0xffL) << 24)
              | ((buf[offset+2] & 0xffL) << 16)
              | ((buf[offset+1] & 0xffL) << 8)
              | ((buf[offset  ] & 0xffL));      // no shift needed
  }

  /** Returns the MurmurHash3_x86_32 hash. */
  private int murmurhash3_x86_32(byte[] data, int offset, int len, int seed) {
    final int c1 = 0xcc9e2d51;
    final int c2 = 0x1b873593;

    int h1 = seed;
    int roundedEnd = offset + (len & 0xfffffffc);  // round down to 4 byte block

    for (int i=offset; i<roundedEnd; i+=4) {
      // little endian load order
      int k1 = (data[i] & 0xff) | ((data[i+1] & 0xff) << 8) | ((data[i+2] & 0xff) << 16) | (data[i+3] << 24);
      k1 *= c1;
      k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
      k1 *= c2;

      h1 ^= k1;
      h1 = (h1 << 13) | (h1 >>> 19);  // ROTL32(h1,13);
      h1 = h1*5+0xe6546b64;
    }

    // tail
    int k1 = 0;

    switch(len & 0x03) {
      case 3:
        k1 = (data[roundedEnd + 2] & 0xff) << 16;
        // fallthrough
      case 2:
        k1 |= (data[roundedEnd + 1] & 0xff) << 8;
        // fallthrough
      case 1:
        k1 |= (data[roundedEnd] & 0xff);
        k1 *= c1;
        k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
        k1 *= c2;
        h1 ^= k1;
    }

    // finalization
    h1 ^= len;

    // fmix(h1);
    h1 ^= h1 >>> 16;
    h1 *= 0x85ebca6b;
    h1 ^= h1 >>> 13;
    h1 *= 0xc2b2ae35;
    h1 ^= h1 >>> 16;

    return h1;
  }

  /** Returns the MurmurHash3_x64_128 hash, placing the result in "out". */
  private void murmurhash3_x64_128(byte[] key, int offset, int len, int seed, LongPair out) {
    // The original algorithm does have a 32 bit unsigned seed.
    // We have to mask to match the behavior of the unsigned types and prevent sign extension.
    long h1 = seed & 0x00000000FFFFFFFFL;
    long h2 = seed & 0x00000000FFFFFFFFL;

    final long c1 = 0x87c37b91114253d5L;
    final long c2 = 0x4cf5ad432745937fL;

    int roundedEnd = offset + (len & 0xFFFFFFF0);  // round down to 16 byte block
    for (int i=offset; i<roundedEnd; i+=16) {
      long k1 = getLongLittleEndian(key, i);
      long k2 = getLongLittleEndian(key, i+8);
      k1 *= c1; k1  = Long.rotateLeft(k1,31); k1 *= c2; h1 ^= k1;
      h1 = Long.rotateLeft(h1,27); h1 += h2; h1 = h1*5+0x52dce729;
      k2 *= c2; k2  = Long.rotateLeft(k2,33); k2 *= c1; h2 ^= k2;
      h2 = Long.rotateLeft(h2,31); h2 += h1; h2 = h2*5+0x38495ab5;
    }

    long k1 = 0;
    long k2 = 0;

    switch (len & 15) {
      case 15: k2  = (key[roundedEnd+14] & 0xffL) << 48;
      case 14: k2 |= (key[roundedEnd+13] & 0xffL) << 40;
      case 13: k2 |= (key[roundedEnd+12] & 0xffL) << 32;
      case 12: k2 |= (key[roundedEnd+11] & 0xffL) << 24;
      case 11: k2 |= (key[roundedEnd+10] & 0xffL) << 16;
      case 10: k2 |= (key[roundedEnd+ 9] & 0xffL) << 8;
      case  9: k2 |= (key[roundedEnd+ 8] & 0xffL);
        k2 *= c2; k2  = Long.rotateLeft(k2, 33); k2 *= c1; h2 ^= k2;
      case  8: k1  = ((long)key[roundedEnd+7]) << 56;
      case  7: k1 |= (key[roundedEnd+6] & 0xffL) << 48;
      case  6: k1 |= (key[roundedEnd+5] & 0xffL) << 40;
      case  5: k1 |= (key[roundedEnd+4] & 0xffL) << 32;
      case  4: k1 |= (key[roundedEnd+3] & 0xffL) << 24;
      case  3: k1 |= (key[roundedEnd+2] & 0xffL) << 16;
      case  2: k1 |= (key[roundedEnd+1] & 0xffL) << 8;
      case  1: k1 |= (key[roundedEnd  ] & 0xffL);
        k1 *= c1; k1  = Long.rotateLeft(k1,31); k1 *= c2; h1 ^= k1;
    }

    //----------
    // finalization

    h1 ^= len; h2 ^= len;

    h1 += h2;
    h2 += h1;

    h1 = fmix64(h1);
    h2 = fmix64(h2);

    h1 += h2;
    h2 += h1;

    out.val1 = h1;
    out.val2 = h2;
  }
}
