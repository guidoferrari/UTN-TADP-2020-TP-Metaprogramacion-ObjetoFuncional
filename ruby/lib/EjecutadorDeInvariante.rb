require 'sourcify'
class EjecutadorDeInvariante

  def self.ejecutar_invariantes(contexto, invariantes)
    invariantes.each {|invariante| ejecutar_invariante(contexto, invariante)}
  end

  def self.ejecutar_invariante(contexto, invariante)
    raise 'Fallo el invariante '+ block_to_s(&invariante.bloque) unless (evaluar_invariante(contexto, invariante))
  end

  def self.evaluar_invariante(contexto, invariante)
    begin
      contexto.instance_exec &invariante.bloque
    rescue NoMethodError => e
      true
    end
  end

  def self.block_to_s(&blk)
    blk.to_source(:strip_enclosure => true)
  end
end