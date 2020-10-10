describe Persona do

  describe '#invariante' do
    it 'Genero una persona que cumple con invariantes' do
      persona = Persona.new("George", "Carlin", "Manhattan, New York")
      expect(persona.lugarDeNacimiento).to eql("Manhattan, New York")
    end

    it 'Genero una persona con lugar de nacimiento nil, debe romper invariante' do
      expect{Persona.new("Rodrigo", "Vagoneta", nil)}.to raise_error('Invariant failed: (not lugarDeNacimiento.nil?)')
    end
  end
end