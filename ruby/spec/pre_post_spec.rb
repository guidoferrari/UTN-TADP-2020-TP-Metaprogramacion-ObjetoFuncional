describe MiClase do
  let(:miClase) { MiClase.new}

  describe '#beforeAndAfter' do
    it 'debería ejecutar el proc antes, el método, y por último el proc después' do
      $stdout = StringIO.new

      miClase.mensaje_1

      salida = $stdout.string.split("\n")
      expect(salida[0]).to eq("Entré a un mensaje")
      expect(salida[1]).to eq("mensaje_1")
      expect(salida[2]).to eq("Salí de un mensaje" )
    end
  end
end