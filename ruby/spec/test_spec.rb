describe Prueba do
  let(:prueba) { Prueba.new }

  describe '#materia' do
    it 'debería ejecutar el proc antes, el método, y por último el proc después' do
      $stdout = StringIO.new

      prueba.materia

      salida = $stdout.string.split("\n")
      expect(salida[0]).to eq('Ejecuté esto antes')
      expect(salida[1]).to eq('ejecutando')
      expect(salida[2]).to eq('Ejecuté esto después')
    end
  end
end