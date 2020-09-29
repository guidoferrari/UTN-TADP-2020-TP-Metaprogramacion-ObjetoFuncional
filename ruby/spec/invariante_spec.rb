describe Guerrero do
  let(:guerrero) { Guerrero.new }

  describe '#invariante' do
    it 'debería funcionar por cumplir los invariantes' do
      guerrero.vida = 100
      guerrero.fuerza = 10

      atacante = Guerrero.new
      atacante.vida = 200
      atacante.fuerza = 20
      atacante.atacar(guerrero)
    end

    it 'debería lanzar error por no cumplir el invariante de vida' do
      guerrero.vida = 100
      guerrero.fuerza = 10

      atacante = Guerrero.new
      expect { atacante.vida = -200 }.to raise_error
    end

    it 'debería lanzar error por no cumplir el invariante de fuerza' do
      guerrero.vida = 100
      guerrero.fuerza = 99

      atacante = Guerrero.new
      atacante.vida = 200
      expect { atacante.fuerza = 2000 }.to raise_error
    end

  # Qué pasa con invariantes sobre objetos afectados? Ej. la validación sobre vida del que recibe daño
  end
end