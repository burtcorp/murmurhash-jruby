# MurmurHash

A native Java implementation of MurmurHash3 for JRuby.

## Usage

Currently only a 64 bit hash is available.

```ruby
# create a new instance with a seed
hasher = MurmurHash::MurmurHash3.new(234364563234234)
# hash a string to a 64 bit number
hasher.hash64('hello world') # => 232351117120425316
# you can also use the class method
MurmurHash::MurmurHash3.hash64('hello world', 234364563234234) # => 232351117120425316
```

## Copyright

Copyright © 2016, Burt AB and contributors. See [See LICENSE.txt](LICENSE.txt). Also contains public domain code from https://github.com/yonik/java_util
