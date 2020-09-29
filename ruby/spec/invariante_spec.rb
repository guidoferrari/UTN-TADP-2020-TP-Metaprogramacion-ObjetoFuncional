describe Guerrero do
  let(:guerrero) { Guerrero.new }

  describe '#invariante' do
    it 'Ataco a un guerrero correctamente sin romper reglas de invariantes' do
      guerrero.vida = 100
      guerrero.fuerza = 10

      atacante = Guerrero.new
      atacante.vida = 200
      atacante.fuerza = 20
      atacante.atacar(guerrero)
    end

    it 'Luego de crear el guerrero, le asigno vida negativa y lanza error' do
      expect { guerrero.vida = -200 }.to raise_error 'Fallo el invariante (vida >= 0)'
    end

    it 'Luego de crear el guerrero, le asigno fuerza inválida y lanza error' do
      guerrero.vida = 100
      expect { guerrero.fuerza = 2000 }.to raise_error 'Fallo el invariante (fuerza > 0) and (fuerza < 100)'
    end

  # Qué pasa con invariantes sobre objetos afectados? Ej. la validación sobre vida del que recibe daño
  end
end