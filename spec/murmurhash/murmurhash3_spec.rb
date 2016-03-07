module MurmurHash
  describe MurmurHash3 do
    let :murmurhash3 do
      described_class.new
    end

    describe '.hash64' do
      it 'returns a 64 bit hash from the given string' do
        expect(murmurhash3.hash64('foobar')).to be_a(Fixnum)
      end

      it 'allows specifying a seed' do
        expect(murmurhash3.hash64('foobar', 0)).to eq(-4768557254695167419)
        expect(murmurhash3.hash64('foobar', 1)).not_to eq(murmurhash3.hash64('foobar', 2))
      end
    end
  end
end
