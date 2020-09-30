describe Operaciones do
    let(:operaciones) { Operaciones.new}

    describe '#pre' do
      it 'Ejecuta el pre sólo para el método asociado' do

        operaciones.dividir(10,2)
=begin
        $stdout = StringIO.new

        operaciones.dividir(10,2)
        operaciones.dividir(10,2)
        operaciones.restar(10,2)
        operaciones.restar(10,2)
        operaciones.dividir(10,2)
        operaciones.restar(10,2)
        salida = $stdout.string.split("\n")

        expect(salida[0]).to eq("pre")
        expect(salida[1]).to eq("dividir")
        expect(salida[2]).to eq("pre")
        expect(salida[3]).to eq("dividir")
        expect(salida[4]).to eq("restar")
        expect(salida[5]).to eq("restar")
        expect(salida[6]).to eq("pre")
        expect(salida[7]).to eq("dividir")
        expect(salida[8]).to eq("restar")
=end
      end
      #it 'Divido correctamente sin romper condiciones' do
        #expect(operaciones.dividir(10,2)).to be(5)
        #end

      #it 'Resto correctamente, no hay validaciones' do
      #expect(operaciones.restar(10,2)).to be(8)
      #end

      #it 'Divido por 0, debe romper precondición' do
      #expect{operaciones.dividir(10,0)}.to raise_error 'Failed to meet precondition'
      #end
  end
end
