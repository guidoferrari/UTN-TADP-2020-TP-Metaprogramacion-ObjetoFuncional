describe Guerrero do
  let(:guerrero) { Guerrero.new }

  describe '#invariante' do
    it 'debería validar los invariantes' do
      guerrero.vida = 200

      atacante = Guerrero.new
      atacante.fuerza = 20
      atacante.atacar(guerrero)
    end
  end
end