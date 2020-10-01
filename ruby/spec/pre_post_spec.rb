describe Operaciones do
  let(:operaciones) { Operaciones.new}

  describe '#beforeAndAfter' do
    it 'Divido correctamente sin romper condiciones' do
      expect(operaciones.dividir(10,2)).to be(5)
    end

    it 'Resto correctamente, no hay validaciones' do
      expect(operaciones.restar(10,2)).to be(8)
    end

    it 'Divido por 0, debe romper precondición' do
      expect{operaciones.dividir(10,0)}.to raise_error 'Failed to meet precondition'
    end

    it 'Divido un número y no me da entero, me debe romper la postcondicion' do
      expect{operaciones.dividir(10,4)}.to raise_error 'Failed to meet postcondition'
    end
  end
end