module MurmurHash
  describe MurmurHash3 do
    describe '#hash64' do
      it 'returns a 64 bit hash from the given string' do
        expect(described_class.new.hash64('foobar')).to be_a(Fixnum)
      end

      it 'allows specifying a seed' do
        expect(described_class.new(0).hash64('foobar')).to eq(-4768557254695167419)
        expect(described_class.new(1).hash64('foobar')).not_to eq(described_class.new(0).hash64('foobar'))
      end
    end

    describe '.hash64' do
      it 'returns a 64 bit hash from the given string' do
        expect(described_class.hash64('foobar')).to be_a(Fixnum)
      end

      it 'allows specifying a seed' do
        expect(described_class.hash64('foobar', 0)).to eq(-4768557254695167419)
        expect(described_class.hash64('foobar', 1)).not_to eq(described_class.hash64('foobar', 0))
      end
    end
  end
end
