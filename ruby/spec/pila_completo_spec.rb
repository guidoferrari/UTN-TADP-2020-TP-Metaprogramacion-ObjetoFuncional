describe Pila do

  mensajePrecondicion = 'Failed to meet precondition'

  describe '#Pila completo' do

    it 'Genero una pila con capacidad negativa, debe romper el invariante' do
      expect {Pila.new(-1)}.to raise_error 'Invariant failed: (capacity >= 0)'
    end

    it 'Intento pushear en una pila pasando su capacidad, debe romper el pre' do
      pila = Pila.new(1)
      pila.push("Un elemento")
      expect{pila.push("Otro elemento")}.to raise_error mensajePrecondicion
    end

    it 'Intento hacer pop en una pila vacía, debe romper el pre' do
      pila = Pila.new(1)
      expect{pila.pop}.to raise_error mensajePrecondicion
    end

    it 'Agrego 2 nodos a una pila vacía, por lo cual su altura debe ser 2' do
      pila = Pila.new(4)
      pila.push("Un elemento")
      pila.push("Otro elemento")
      expect(pila.height).to be(2)
    end
  end
end