describe Guerrero do
  let(:guerrero) { Guerrero.new(70, 50) }

  describe '#invariante' do
    it 'debería validar los invariantes' do

      atacante = Guerrero.new(50, 80)
      atacante.atacar(guerrero)
      expect(atacante.atacar(guerrero)).to raise_error(RuntimeError)
    end
  end
end