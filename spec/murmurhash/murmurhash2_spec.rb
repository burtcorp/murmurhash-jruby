module MurmurHash
  describe MurmurHash2 do
    describe '#hash32' do
      it 'returns a 32 bit hash from the given string' do
        expect(described_class.new.hash32('foobar')).to be_a(Fixnum)
      end

      it 'allows specifying a seed' do
        expect(described_class.new(0).hash32('foobar')).to eq(1729472814)
        expect(described_class.new(1).hash32('foobar')).not_to eq(described_class.new(0).hash32('foobar'))
      end
    end

    describe '.hash32' do
      it 'returns a 32 bit hash from the given string' do
        expect(described_class.hash32('foobar')).to be_a(Fixnum)
      end

      it 'allows specifying a seed' do
        expect(described_class.hash32('foobar', 0)).to eq(1729472814)
        expect(described_class.hash32('foobar', 1)).not_to eq(described_class.hash32('foobar', 0))
      end
    end
  end
end
