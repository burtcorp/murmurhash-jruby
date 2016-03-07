# encoding: utf-8

lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)

require 'murmurhash/version'

Gem::Specification.new do |spec|
  spec.name = 'murmurhash-jruby'
  spec.version = MurmurHash::VERSION
  spec.platform = 'java'
  spec.authors = ['Theo Hultberg']
  spec.email = ['theo@burtcorp.com']
  spec.summary = %q{MurmurHash3 as a Java extension for JRuby}
  spec.description = %q{A native Java implementation of MurmurHash3 packaged as a JRuby extension}
  spec.homepage = 'https://github.com/burtcorp/murmurhash-jruby'
  spec.license = 'BSD-3-Clause'

  spec.files = Dir['lib/**/*.{rb,jar}'] + Dir['bin/*'] + %w[LICENSE.txt README.md]
  spec.executables = %w[]
  spec.require_paths = %w[lib]

  spec.add_development_dependency 'bundler', '~> 1.11'
end
