describe Operaciones do
  let(:operaciones) { Operaciones.new }

  describe 'Debe romper si el divisor es 0' do
    it 'debería pasar este test' do
      expect{operaciones.dividir(10,0)}.to raise_error(RuntimeError)
    end
  end

  describe 'Debe dividir correctamente' do
    it 'debería pasar este test' do
      expect(operaciones.dividir(6,2)).to be(3)
    end
  end

  describe 'Debe restar correctamente' do
    it 'debería pasar este test' do
      expect(operaciones.restar(6,2)).to be(4)
    end
  end
end