# MurmurHash

Native Java implementations of MurmurHash2 and MurmurHash3 for JRuby.

## Usage

You can hash strings either by creating a hasher instance or using class methods. Depending on which hash you want to use there are different methods that produce hashes of different lengths. MurmurHash2 only has a 32 bit hash, but MurmurHash3 has 32, 64 and 128 bit hashes (though the most commonly used the 64 bit hash actually uses half of the 128 bit hash's output).

Creating an instance is convenient if you want to specify the seed and hash many times:

```ruby
hasher = MurmurHash::MurmurHash3.new(234364563234234)
hasher.hash64('hello world') # => 232351117120425316
hasher.hash32('hello world') # => 1711414565
```

You can also use class methods:

```ruby
MurmurHash::MurmurHash3.hash64('hello world') # => 4082196346333133779
```

You can also specify the seed when using class methods:

```ruby
MurmurHash::MurmurHash3.hash64('hello world', 234364563234234) # => 232351117120425316
```

## Build and run tests

If you want to build the library yourself, for example to prepare a pull request look at `.travis.yml` to see how the code is built and the tests are run in Travis.

## Copyright

Copyright © 2016, Burt AB and contributors. See [See LICENSE.txt](LICENSE.txt). Also contains public domain code from https://github.com/yonik/java_util
