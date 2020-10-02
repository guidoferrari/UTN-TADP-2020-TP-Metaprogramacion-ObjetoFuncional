describe MiClase do
  let(:miClase) { MiClase.new}

  describe '#beforeAndAfter' do
    it 'debería ejecutar el proc antes, el método, y por último el proc después' do
      $stdout = StringIO.new

      miClase.mensaje_1
      miClase.mensaje_2
      miClase.mensaje_1

      salida = $stdout.string.split("\n")
      expect(salida[0]).to eq("Entré a un mensaje")
      expect(salida[1]).to eq("mensaje_1")
      expect(salida[2]).to eq("Salí de un mensaje" )
      expect(salida[3]).to eq("Entré a un mensaje")
      expect(salida[4]).to eq("mensaje_2")
      expect(salida[5]).to eq("Salí de un mensaje" )
      expect(salida[6]).to eq("Entré a un mensaje")
      expect(salida[7]).to eq("mensaje_1")
      expect(salida[8]).to eq("Salí de un mensaje" )
    end
  end
end