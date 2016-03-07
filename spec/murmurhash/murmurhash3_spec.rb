module MurmurHash
  describe MurmurHash3 do
    describe '#hash32' do
      it 'returns a 32 bit hash from the given string' do
        expect(described_class.new.hash32('foobar')).to be_a(Fixnum)
      end

      it 'allows specifying a seed' do
        expect(described_class.new(0).hash32('foobar')).to eq(-1530604355)
        expect(described_class.new(1).hash32('foobar')).not_to eq(described_class.new(0).hash32('foobar'))
      end
    end

    describe '.hash32' do
      it 'returns a 32 bit hash from the given string' do
        expect(described_class.hash32('foobar')).to be_a(Fixnum)
      end

      it 'allows specifying a seed' do
        expect(described_class.hash32('foobar', 0)).to eq(-1530604355)
        expect(described_class.hash32('foobar', 1)).not_to eq(described_class.hash32('foobar', 0))
      end
    end

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

    describe '#hash128' do
      it 'returns a 128 bit hash from the given string' do
        expect(described_class.new.hash128('foobar')).to be_a(Bignum)
      end

      it 'allows specifying a seed' do
        expect(described_class.new(0).hash128('foobar')).to eq(92188096686550789763322207219261743732)
        expect(described_class.new(1).hash128('foobar')).not_to eq(described_class.new(0).hash128('foobar'))
      end
    end

    describe '.hash128' do
      it 'returns a 128 bit hash from the given string' do
        expect(described_class.hash128('foobar')).to be_a(Bignum)
      end

      it 'allows specifying a seed' do
        expect(described_class.hash128('foobar', 0)).to eq(92188096686550789763322207219261743732)
        expect(described_class.hash128('foobar', 1)).not_to eq(described_class.hash128('foobar', 0))
      end
    end
  end
end
